package invoice.com.demo.features.products.dto;

import invoice.com.demo.domain.Currency;

import java.math.BigDecimal;

public record ProductRequest (
        String name,
        String image_url,
        BigDecimal price,
        Currency currency_type,
        Long productTypeId,
        Integer quantity,
        Integer low_stock ,
        String description
){
}
