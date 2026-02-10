package invoice.com.demo.features.products.dto;

import invoice.com.demo.domain.Currency;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String uuid,
        String name,
        String image_url,
        BigDecimal price,
        Currency currency_type,
        String status,
        // Product Type
        Long productTypeId,
        String productTypeName,

        Integer stockQuantity,
        Integer low_stock,
        String description,

        // Created By
        Long userId
) {
}
