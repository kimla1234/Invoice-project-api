package invoice.com.demo.features.invoice.dto;

import invoice.com.demo.domain.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceRequest (
        Long clientId,
        double subtotal,
        double tax,
        double grandTotal
) {}