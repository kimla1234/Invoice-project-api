package invoice.com.demo.features.products;

import invoice.com.demo.features.products.dto.ProductRequest;
import invoice.com.demo.features.products.dto.ProductResponse;
import invoice.com.demo.features.products.dto.ProductUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;


public interface ProductService  {
    List<ProductResponse> findAllProduct ();
    List<ProductResponse> getMyProducts (Jwt jwt);
    ProductResponse createProduct(ProductRequest request , Jwt jwt);
    void deleteProductByUuid(String uuid);
    ProductResponse getProductByUuid(String uuid);
    ProductResponse getProductById(Long id);
    ProductResponse updateProductByUuid(String uuid , @Valid ProductUpdateRequest productUpdateRequest );


}
