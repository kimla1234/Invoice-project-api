package invoice.com.demo.features.invoice.dto;

import invoice.com.demo.domain.Currency;
import invoice.com.demo.domain.InvoiceItem;
import invoice.com.demo.features.invoiceitems.dto.InvoiceItemRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceRequest (
        Long clientId,
        double subtotal,
        double tax,
        double grandTotal,
        String status,
        List<InvoiceItemRequest> items  // ← Add this

) {}