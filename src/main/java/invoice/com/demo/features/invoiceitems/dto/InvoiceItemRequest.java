package invoice.com.demo.features.invoiceitems.dto;


public record InvoiceItemRequest(
        Long invoiceId,
        Long productId,
        double unitPrice,
        double quantity,
        double subtotal
) {
}
