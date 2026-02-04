package invoice.com.demo.features.stocks;

import invoice.com.demo.features.stocks.dto.StockRequest;

public interface StockService  {
    void handleStockMovement(StockRequest request);
}
