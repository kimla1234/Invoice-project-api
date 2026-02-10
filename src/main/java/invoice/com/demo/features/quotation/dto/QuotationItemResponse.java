package invoice.com.demo.features.quotation.dto;

import lombok.Data;

@Data
public class QuotationItemResponse {
    private Long id;
    private Long quotationId;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double lineTotal;
}
