package invoice.com.demo.features.invoice.dto;

import invoice.com.demo.domain.Client;
import invoice.com.demo.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public record InvoiceResponse (
        Long id,
        Long userId,
        Long clientId,
        double subtotal,
        double grandTotal,
        double tax,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
