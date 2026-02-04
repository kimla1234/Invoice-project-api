package invoice.com.demo.features.stocks;

import invoice.com.demo.features.stocks.dto.StockRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    @PostMapping("/movement")
    public ResponseEntity<?> updateStock(@RequestBody StockRequest request) {
        stockService.handleStockMovement(request);
        return ResponseEntity.ok("Stock updated successfully");
    }
}
