package invoice.com.demo.features.stocks;

import invoice.com.demo.features.stocks.dto.StockRequest;
import invoice.com.demo.features.stocks.dto.StockResponse;

public interface StockService  {
    StockResponse handleStockMovement(StockRequest request);
}
