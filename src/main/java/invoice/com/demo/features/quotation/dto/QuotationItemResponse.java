package invoice.com.demo.features.quotation.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationItemResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
