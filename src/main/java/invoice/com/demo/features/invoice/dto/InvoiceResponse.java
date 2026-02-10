package invoice.com.demo.features.invoice.dto;

import invoice.com.demo.domain.InvoiceItem;
import invoice.com.demo.features.invoiceitems.dto.InvoiceItemResponse;

import java.time.LocalDateTime;
import java.util.List;

public record InvoiceResponse (
        Long id,
        Long userId,
        Long clientId,
        String status,
        double subtotal,
        double grandTotal,
        double tax,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<InvoiceItemResponse> items
) {}
