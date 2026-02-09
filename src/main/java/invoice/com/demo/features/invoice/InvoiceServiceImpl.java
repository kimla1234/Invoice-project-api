package invoice.com.demo.features.invoice;

import invoice.com.demo.domain.Client;
import invoice.com.demo.domain.Invoice;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.clients.ClientRepository;
import invoice.com.demo.features.invoice.InvoiceRepository;
import invoice.com.demo.features.invoice.dto.InvoiceRequest;
import invoice.com.demo.features.invoice.dto.InvoiceResponse;
import invoice.com.demo.features.users.UserRepository;
import invoice.com.demo.mapper.InvoiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final InvoiceMapper invoiceMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseEntity<InvoiceResponse> create(InvoiceRequest request, Jwt jwt) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        User user = getUser(jwt);

        Invoice invoice = invoiceMapper.toEntity(request);
        invoice.setClient(client);
        invoice.setUser(user);

        Invoice saved = invoiceRepository.save(invoice);
        return ResponseEntity.ok(invoiceMapper.toResponse(saved));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        // Optional: Add authorization check - ensure user owns this invoice
        // User currentUser = getUser(jwt);
        // if(!invoice.getUser().getId().equals(currentUser.getId())) {
        //     throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized");
        // }

        return invoiceMapper.toResponse(invoice);
    }


    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAll(Jwt jwt) {
        User currentUser = getUser(jwt);

        // Get invoices only for the current user
        List<Invoice> invoices = invoiceRepository.findAllByUser(currentUser);

        return invoices.stream()
                .map(invoiceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InvoiceResponse update(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        // Update client if changed
        if (!invoice.getClient().getId().equals(request.clientId())) {
            Client client = clientRepository.findById(request.clientId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
            invoice.setClient(client);
        }

        // Update other fields using mapper
        invoiceMapper.toEntity(request);

        Invoice updated = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));

        invoiceRepository.delete(invoice);
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