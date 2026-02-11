package invoice.com.demo.features.quotation.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationItemRequest {
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
