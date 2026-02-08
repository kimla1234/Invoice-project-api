package invoice.com.demo.features.stocks;

import invoice.com.demo.features.stocks.dto.MovementDto;
import invoice.com.demo.features.stocks.dto.StockRequest;
import invoice.com.demo.features.stocks.dto.StockResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface StockService  {
    StockResponse handleStockMovement(StockRequest request);
    List<MovementDto> getMovementByProductsUuid (String productUUid );
}
