package invoice.com.demo.features.quotation.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuotationUpdateRequest {

    private Long id;
    private Long userId;
    private Long clientId;

    private LocalDateTime quotationDate;
    private LocalDateTime quotationExpire;

    private List<QuotationItemUpdateRequest> items;
}
