package invoice.com.demo.features.quotation.dto;

import invoice.com.demo.domain.QuotationStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuotationResponse {
    private Long id;
    private Long userId;
    private Long clientId;
    private Long invoiceId;
    private LocalDateTime quotationDate;
    private LocalDateTime quotationExpire;

    private BigDecimal totalAmount;
    private List<QuotationItemResponse> items;
    private QuotationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
