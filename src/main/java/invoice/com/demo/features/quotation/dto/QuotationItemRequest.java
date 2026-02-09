package invoice.com.demo.features.quotation.dto;

import lombok.Data;

@Data
public class QuotationItemRequest {
    private Long productId;
    private Integer quantity;
    private double unitPrice;
    private double lineTotal;
}
