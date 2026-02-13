package invoice.com.demo.features.invoice;

import invoice.com.demo.domain.*;
import invoice.com.demo.features.clients.ClientRepository;
import invoice.com.demo.features.invoice.dto.InvoiceRequest;
import invoice.com.demo.features.invoice.dto.InvoiceResponse;
import invoice.com.demo.features.invoiceitems.InvoiceItemRepository;
import invoice.com.demo.features.invoiceitems.InvoiceItemService;
import invoice.com.demo.features.invoiceitems.dto.InvoiceItemRequest;
import invoice.com.demo.features.products.ProductRepository;
import invoice.com.demo.features.products.ProductService;
import invoice.com.demo.features.stocks.StockMovementRepository;
import invoice.com.demo.features.stocks.StockRepository;
import invoice.com.demo.features.stocks.StocksRepository;
import invoice.com.demo.features.users.UserRepository;
import invoice.com.demo.features.users.UserService;
import invoice.com.demo.mapper.InvoiceItemMapper;
import invoice.com.demo.mapper.InvoiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemMapper invoiceItemMapper;
    private final UserRepository userRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final ProductRepository productRepository;
    private final StockMovementRepository  stockMovementRepository;
    private final StockRepository stockRepository;


    @Override
    @Transactional
    public ResponseEntity<InvoiceResponse> create(InvoiceRequest request, Jwt jwt) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        User user = getUser(jwt);

        Invoice invoice = invoiceMapper.toEntity(request);
        invoice.setClient(client);
        invoice.setUser(user);
        invoice.setStatus("pending");


        // Add items
        if (request.items() != null) {
            for (InvoiceItemRequest itemDto : request.items()) {
                Product product = productRepository.findById(itemDto.productId().toString())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

                InvoiceItem item = new InvoiceItem();
                item.setProduct(product);
                item.setInvoice(invoice);
                item.setUnitPrice(itemDto.unitPrice());
                item.setQuantity(itemDto.quantity());
                item.setSubtotal(itemDto.subtotal());
                item.setName(product.getName());
                invoice.addItem(item);  // Use helper



            }
        }
        Invoice saved = invoiceRepository.save(invoice);

        if (request.items() != null) {
            for (InvoiceItemRequest itemDto : request.items()) {
                Product product = productRepository.findById(itemDto.productId().toString()).get();

                Stocks stock = stockRepository.findByProduct(product)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock not found for product: " + product.getName()));

                if (stock.getQuantity() < itemDto.quantity()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock for: " + product.getName());
                }

                stock.setQuantity(stock.getQuantity() - (int) itemDto.quantity());
                stockRepository.save(stock); //  Save Stocks Entity

                StockMovements movement = new StockMovements();
                movement.setProduct(product);
                movement.setType(StockType.OUT);
                movement.setQuantity((int) itemDto.quantity());
                movement.setNote("Sold via Invoice By: " + saved.getClient().getName());
                movement.setCreatedBy(user.getId());

                stockMovementRepository.save(movement); //  Save StockMovements Entity

            }
        }

        //Invoice saved = invoiceRepository.save(invoice);
        return ResponseEntity.ok(invoiceMapper.toResponse(saved));
    }

    @Override
    public ResponseEntity<InvoiceResponse> getById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        return ResponseEntity.ok(invoiceMapper.toResponse(invoice));
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<Page<InvoiceResponse>> getAll(Jwt jwt, int page, int size) {
        User currentUser = getUser(jwt);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Invoice> invoicesPage = invoiceRepository.findAllByUser(currentUser, pageable);

        Page<InvoiceResponse> responsePage = invoicesPage.map(invoiceMapper::toResponse);

        return ResponseEntity.ok(responsePage);
    }

    @Override
    @Transactional
    public ResponseEntity<InvoiceResponse> update(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        String oldStatus = invoice.getStatus();
        String newStatus = request.status();
        // --- STOCK LOGIC START ---

        // Case 1: Changing TO "cancelled" (Restoring Stock)
        if (!oldStatus.equalsIgnoreCase("cancelled") && newStatus.equalsIgnoreCase("cancelled")) {
            for (InvoiceItem item : invoice.getItems()) {
                Product product = item.getProduct();
                Stocks stock = product.getStock();

                if (stock != null) {
                    // 1. Update numeric stock value
                    double newQty = stock.getQuantity() + item.getQuantity();
                    stock.setQuantity((int) newQty);
                    stockRepository.save(stock);

                    // 2. Record Stock Movement (IN)
                    StockMovements movement = new StockMovements();
                    movement.setProduct(product);
                    movement.setType(StockType.IN); // Stock is coming back
                    movement.setQuantity((int) item.getQuantity());
                    movement.setNote("Stock Restored - Invoice #" + id  + " Cancelled");
                    stockMovementRepository.save(movement);
                }
            }
        }

// Case 2: Changing FROM "cancelled" to something else (Deducting Stock)
        else if (oldStatus.equalsIgnoreCase("cancelled") && !newStatus.equalsIgnoreCase("cancelled")) {
            for (InvoiceItem item : invoice.getItems()) {
                Product product = item.getProduct();
                Stocks stock = product.getStock();

                if (stock != null) {
                    if (stock.getQuantity() < item.getQuantity()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Insufficient stock to restore Invoice #" + id);
                    }

                    // 1. Update numeric stock value
                    double newQty = stock.getQuantity() - item.getQuantity();
                    stock.setQuantity((int) newQty);
                    stockRepository.save(stock);

                    // 2. Record Stock Movement (OUT)
                    StockMovements movement = new StockMovements();
                    movement.setProduct(product);
                    movement.setType(StockType.OUT); // Stock is going out again
                    movement.setQuantity((int) item.getQuantity());
                    movement.setNote("Stock Deducted - Invoice #" + id + " Re-activated");
                    stockMovementRepository.save(movement);
                }
            }
        }
        // --- STOCK LOGIC END ---

        // Update client if changed
        if (!invoice.getClient().getId().equals(request.clientId())) {
            Client client = clientRepository.findById(request.clientId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));


            invoice.setClient(client);
        }

        // Update invoice fields
        LocalDateTime issueDate = LocalDate.parse(request.issueDate()).atStartOfDay();
        LocalDateTime expireDate = LocalDate.parse(request.expireDate()).atStartOfDay();

        invoice.setSubtotal(request.subtotal());
        invoice.setTax(request.tax());
        invoice.setGrandTotal(request.grandTotal());
        invoice.setStatus(request.status());
        invoice.setIssueDate(issueDate);
        invoice.setExpireDate(expireDate);
        // Update items - remove old items and add new ones
        invoice.getItems().clear();

        if (request.items() != null) {
            for (InvoiceItemRequest itemDto : request.items()) {
                Product product = productRepository.findById(itemDto.productId().toString())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

                InvoiceItem item = new InvoiceItem();
                item.setProduct(product);
                item.setUnitPrice(itemDto.unitPrice());
                item.setQuantity(itemDto.quantity());
                item.setSubtotal(itemDto.subtotal());
                invoice.addItem(item);
            }
        }

        Invoice updated = invoiceRepository.save(invoice);
        return ResponseEntity.ok(invoiceMapper.toResponse(updated));
    }

    @Override
    @Transactional
    public ResponseEntity<Boolean> delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        invoiceRepository.delete(invoice);
        return ResponseEntity.ok(true);
    }

    @Override
    public ResponseEntity<String> removeItem(Long id, Long itemId) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);
        if (invoiceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Invoice invoice = invoiceOpt.get();
        Optional<InvoiceItem> itemOpt = invoiceItemRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        invoice.removeItem(itemOpt.get());
        invoiceRepository.save(invoice);

        return ResponseEntity.ok("Item removed successfully");
    }

    @Override
    public ResponseEntity<InvoiceResponse> addItem(Long id, InvoiceItemRequest invoiceItemRequest) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);
        if (invoiceOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Product> productOpt = productRepository.findById(invoiceItemRequest.productId().toString());
        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Invoice invoice = invoiceOpt.get();
        Product product = productOpt.get();

        InvoiceItem item = invoiceItemMapper.toEntity(invoiceItemRequest);
        item.setProduct(product);

        invoice.addItem(item);

        invoiceRepository.save(invoice);

        return ResponseEntity.ok(invoiceMapper.toResponse(invoice));
    }

    // should use AuthService
    private User getUser(Jwt jwt) {
        // Try email claim first, then subject claim
        String userIdentifier = jwt.getClaimAsString("email");
        if (userIdentifier == null || userIdentifier.isEmpty()) {
            userIdentifier = jwt.getSubject(); // fallback to subject claim
        }

        if (userIdentifier == null || userIdentifier.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to identify user from JWT");
        }

        return userRepository.findByEmail(userIdentifier)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }



}