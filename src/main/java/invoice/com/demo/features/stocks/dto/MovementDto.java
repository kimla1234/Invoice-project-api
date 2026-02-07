package invoice.com.demo.features.stocks.dto;

public record MovementDto(
        String productUuid,
        String type,
        int quantity,
        String note
) {
}
