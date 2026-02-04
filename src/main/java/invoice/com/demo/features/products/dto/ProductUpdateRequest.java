package invoice.com.demo.features.products.dto;

import java.math.BigDecimal;

public record ProductUpdateRequest(
        String name,
        String image_url,
        BigDecimal price,
        Long productTypeId,
        Boolean status
) {
}