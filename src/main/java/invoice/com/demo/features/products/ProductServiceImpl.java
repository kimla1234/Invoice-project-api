package invoice.com.demo.features.products;

import invoice.com.demo.domain.*;
import invoice.com.demo.features.products.dto.ProductRequest;
import invoice.com.demo.features.products.dto.ProductResponse;
import invoice.com.demo.features.products.dto.ProductUpdateRequest;
import invoice.com.demo.features.stocks.StockMovementRepository;
import invoice.com.demo.features.stocks.StocksRepository;
import invoice.com.demo.features.users.UserRepository;
import invoice.com.demo.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final ProductTypeRepository productTypeRepository;
    private final StocksRepository stocksRepository;
    private final StockMovementRepository stockMovementRepository;

    @Override
    public List<ProductResponse> findAllProduct() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getMyProducts(Jwt jwt) {
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        String email = jwt.getClaimAsString("iss");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Product> products = productRepository.findByUserAndIsDeletedFalse(user);

        if (products.isEmpty()) {

            return Collections.emptyList();
        }
        products.forEach(Product::updateStatusFromStock);

        return productMapper.toProductResponseList(products);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request , Jwt jwt ) {
        //Check ProductType
        ProductType productType = productTypeRepository.findById(request.productTypeId())
                .orElseThrow(() -> new RuntimeException("Product type not found"));

        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String email = jwt.getClaimAsString("iss");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        //  DTO to Entity
        Product product = productMapper.toEntity(request);
        product.setProductType(productType);
        product.setUser(user);

        // Save Product
        Product savedProduct = productRepository.save(product);

        // Stock ( quantity fromm request)
        Stocks stock = new Stocks();
        stock.setProduct(savedProduct);
        stock.setLow_stock(request.low_stock());
        stock.setQuantity(request.quantity() != null ? request.quantity() : 0);
        stocksRepository.save(stock);

        savedProduct.setStock(stock);
        // ៣. ឆែក Status ដោយស្វ័យប្រវត្តិ
        product.updateStatusFromStock();

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public void deleteProductByUuid(String uuid) {
       Product product = productRepository.findByUuid(uuid)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));


       product.setIsDeleted(true);
       //product.setStatus(false);
       productRepository.save(product);
    }

    @Override
    public ProductResponse getProductByUuid(String uuid) {

        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));


        return productMapper.toResponse(product);
    }


    @Override
    @Transactional
    public ProductResponse updateProductByUuid(String uuid, ProductUpdateRequest productUpdateRequest) {
        // Check Product
        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        // Update Product
        if (productUpdateRequest.name() != null) product.setName(productUpdateRequest.name());
        if (productUpdateRequest.price() != null) product.setPrice(productUpdateRequest.price());
        if (productUpdateRequest.currency_type() != null) product.setCurrency_type(productUpdateRequest.currency_type());
        if (productUpdateRequest.image_url() != null) product.setImage_url(productUpdateRequest.image_url());
        if (productUpdateRequest.description() != null)
            product.setDescription(productUpdateRequest.description());
        if (productUpdateRequest.low_stock() != null) {
            if (product.getStock() == null) {
                Stocks stock = new Stocks();
                stock.setProduct(product);
                product.setStock(stock);
            }
            product.getStock().setLow_stock(productUpdateRequest.low_stock());
        }


        return productMapper.toResponse(productRepository.save(product));
    }
}
