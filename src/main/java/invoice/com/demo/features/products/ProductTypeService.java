package invoice.com.demo.features.products;

import invoice.com.demo.features.products.dto.ProductResponse;
import invoice.com.demo.features.products.dto.ProductTypeRequest;
import invoice.com.demo.features.products.dto.ProductTypeResponse;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ProductTypeService {
    // Create a new type (linked to the user in the JWT)
    ProductTypeResponse createProductType(ProductTypeRequest request, Jwt jwt);
    List<ProductTypeResponse> getMyProductType (Jwt jwt);
}
