package invoice.com.demo.features.quotation.dto;

import lombok.Data;

@Data
public class QuotationItemUpdateRequest {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
}
