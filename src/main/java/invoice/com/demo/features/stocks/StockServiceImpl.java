package invoice.com.demo.features.stocks;

import invoice.com.demo.domain.Product;
import invoice.com.demo.domain.StockMovements;
import invoice.com.demo.domain.StockType;
import invoice.com.demo.domain.Stocks;
import invoice.com.demo.features.products.ProductRepository;
import invoice.com.demo.features.stocks.dto.StockRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StocksRepository stocksRepository;
    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void handleStockMovement(StockRequest request) {
        // Check Product
        Product product = productRepository.findByUuid(request.productUuid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));


        Stocks stock = product.getStock();
        int currentQty = stock.getQuantity();
        int inputQty = request.quantity();
        StockType movementType = StockType.valueOf(request.type().toUpperCase());

        int finalQty = switch (movementType) {
            case IN -> currentQty + inputQty;
            case OUT -> {
                if (currentQty < inputQty) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient stock!");
                }
                yield currentQty - inputQty;
            }
            case ADJUST -> inputQty;
        };

        // Movement (Log)
        StockMovements movement = new StockMovements();
        movement.setProduct(product);
        movement.setType(movementType);
        movement.setQuantity(inputQty);
        movement.setNote(request.note());

        stockMovementRepository.save(movement);

        // Update Table Stocks
        stock.setQuantity(finalQty);
        stocksRepository.save(stock);
    }
}