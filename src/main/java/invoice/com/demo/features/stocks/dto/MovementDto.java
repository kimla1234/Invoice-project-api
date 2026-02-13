package invoice.com.demo.features.stocks.dto;

import java.time.LocalDateTime;

public record MovementDto(
        String productUuid,
        String type,
        int quantity,
        String note,
        Long createdBy,
        LocalDateTime created_at
) {
}
