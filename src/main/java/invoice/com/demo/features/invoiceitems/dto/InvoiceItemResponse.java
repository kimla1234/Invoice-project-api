package invoice.com.demo.features.invoiceitems.dto;

public record InvoiceItemResponse(
        Long id,
        Long invoiceId,
        Long productId,
        double unitPrice,
        double quantity,
        double subtotal,
        String createdAt,
        String updatedAt
) {
}

