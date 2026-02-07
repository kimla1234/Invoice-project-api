package invoice.com.demo.features.stocks.dto;

import java.util.List;

public record StockResponse(
        Integer stockQuantity,
        List<MovementDto> movements
) {
}
