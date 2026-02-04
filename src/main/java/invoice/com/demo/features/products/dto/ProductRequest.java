package invoice.com.demo.features.products.dto;

import java.math.BigDecimal;

public record ProductRequest (
        String name,
        String image_url,
        BigDecimal price,
        Long productTypeId,
        Integer quantity
){
}
