package invoice.com.demo.mapper;

import invoice.com.demo.domain.Product;
import invoice.com.demo.domain.ProductType;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.products.dto.ProductRequest;
import invoice.com.demo.features.products.dto.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = { UUID.class })
public interface ProductMapper {

    @Mapping(target = "uuid", expression = "java(UUID.randomUUID().toString())")
    @Mapping(target = "productType", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "stock", ignore = true)
    Product toEntity(ProductRequest productRequest);

    @Mapping(source = "productType.id", target = "productTypeId")
    @Mapping(source = "productType.name", target = "productTypeName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "image_url", target = "imageUrl")
    @Mapping(source = "stock.quantity", target = "stockQuantity")
    ProductResponse toResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);
}