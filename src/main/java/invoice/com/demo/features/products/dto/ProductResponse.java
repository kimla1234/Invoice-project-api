package invoice.com.demo.features.products.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String uuid,
        String name,
        String imageUrl,
        BigDecimal price,
        Boolean status,

        // Product Type
        Long productTypeId,
        String productTypeName,

        Integer stockQuantity,

        // Created By
        Long userId
) {
}
