package invoice.com.demo.features.stocks.dto;

public record StockRequest(
        String productUuid,
        Integer quantity,
        String type, // "IN", "OUT", "ADJUST"
        String note
) {}
