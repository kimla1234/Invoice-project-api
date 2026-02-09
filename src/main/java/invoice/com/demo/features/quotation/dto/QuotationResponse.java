package invoice.com.demo.features.quotation.dto;

import lombok.Data;

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

    private Double totalAmount;
    private List<QuotationItemRequest> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
