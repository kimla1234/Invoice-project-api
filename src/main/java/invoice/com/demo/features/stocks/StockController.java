package invoice.com.demo.features.stocks;

import invoice.com.demo.base.BaseMessage;
import invoice.com.demo.features.products.ProductService;
import invoice.com.demo.features.products.dto.ProductResponse;
import invoice.com.demo.features.stocks.dto.MovementDto;
import invoice.com.demo.features.stocks.dto.StockRequest;
import invoice.com.demo.features.stocks.dto.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    private final ProductService productService;

    @PostMapping("/movement")
    public ResponseEntity<BaseMessage<StockResponse>> updateStock(
            @RequestBody StockRequest request) {

        StockResponse response = stockService.handleStockMovement(request);

        return ResponseEntity.ok(
                new BaseMessage<>("Stock updated", response)
        );
    }


    @GetMapping("/movement/{uuid}")
    public ResponseEntity<BaseMessage<List<MovementDto>>> getByProductUuid(
            @PathVariable String uuid
    ) {
        List<MovementDto> movements = stockService.getMovementByProductsUuid(uuid);

        return ResponseEntity.ok(
                new BaseMessage<>("Movement fetched successfully", movements)
        );
    }






}
