package invoice.com.demo.features.products;

import invoice.com.demo.base.BaseMessage;
import invoice.com.demo.features.products.dto.*;
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
    private final ProductTypeService productTypeService;

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


    // Product Type

    @PostMapping("/type")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ProductTypeResponse createProductType(@RequestBody ProductTypeRequest productTypeRequest , @AuthenticationPrincipal Jwt jwt) {
        return productTypeService.createProductType(productTypeRequest,jwt);
    }

    @GetMapping("/type")
    public BaseMessage<List<ProductTypeResponse>> getMyProductTypes(@AuthenticationPrincipal Jwt jwt) {
        List<ProductTypeResponse> productTypes = productTypeService.getMyProductType(jwt);

        return BaseMessage.<List<ProductTypeResponse>>builder()
                .message("Product types retrieved successfully")
                .data(productTypes)
                .build();
    }


}
