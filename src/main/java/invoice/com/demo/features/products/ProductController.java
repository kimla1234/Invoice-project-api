package invoice.com.demo.features.products;

import invoice.com.demo.domain.Product;
import invoice.com.demo.features.products.dto.ProductRequest;
import invoice.com.demo.features.products.dto.ProductResponse;
import invoice.com.demo.features.products.dto.ProductUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.findAllProduct();
    }

    @PostMapping
    public ProductResponse createProduct(@RequestBody ProductRequest productRequest,@AuthenticationPrincipal Jwt jwt) {
        return productService.createProduct(productRequest, jwt);
    }

    @GetMapping("/my-products")
    public List<ProductResponse> getMyProduct(@AuthenticationPrincipal Jwt jwt) {
        return productService.getMyProducts(jwt);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String uuid) {
         productService.deleteProductByUuid(uuid);
    }

    @GetMapping("/{uuid}")
    public ProductResponse getProduct(@PathVariable String uuid) {
        return productService.getProductByUuid(uuid);
    }

    @PatchMapping("/{uuid}")
    public ProductResponse updateProduct(@PathVariable String uuid, @RequestBody ProductUpdateRequest productUpdateRequest) {
        return  productService.updateProductByUuid(uuid , productUpdateRequest);
    }


}
