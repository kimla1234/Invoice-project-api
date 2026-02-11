package invoice.com.demo.features.quotation.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationItemUpdateRequest {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
}
