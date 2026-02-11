package invoice.com.demo.features.products;

import invoice.com.demo.domain.ProductType;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.products.dto.ProductTypeRequest;
import invoice.com.demo.features.products.dto.ProductTypeResponse;
import invoice.com.demo.features.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService {
    private final ProductTypeRepository productTypeRepository;
    private final UserRepository userRepository;

    @Override
    public ProductTypeResponse createProductType(ProductTypeRequest request, Jwt jwt) {
        String upperName = request.name().trim().toUpperCase();

        // 2. Business Rule: Check if name already exists
        if (productTypeRepository.existsByNameIgnoreCase(upperName)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Product type '" + upperName + "' already exists."
            );
        }

        // Extract user from JWT (assuming 'sub' or a custom claim 'id' holds the UUID)
        String email = jwt.getClaimAsString("iss");
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductType productType = new ProductType();
        productType.setName(upperName);
        productType.setStatus(request.status() != null ? request.status() : true);
        productType.setUser(creator);

        ProductType savedType = productTypeRepository.save(productType);

        // Return the response DTO
        return new ProductTypeResponse(
                savedType.getId(),
                savedType.getName(),
                savedType.getStatus(),
                savedType.getCreatedAt(),
                creator.getName()
        );
    }

    @Override
    public List<ProductTypeResponse> getMyProductType(Jwt jwt) {
        String email = jwt.getClaimAsString("iss");

        // map() logic with a null-safe check for the User object
        return productTypeRepository.findByUserEmail(email).stream()
                .map(type -> new ProductTypeResponse(
                        type.getId(),
                        type.getName(),
                        type.getStatus(),
                        type.getCreatedAt(),
                        // Safe check: if user is null for some reason, return "System" or "Unknown"
                        type.getUser() != null ? type.getUser().getName() : "System"
                ))
                .toList();
    }

    @Override
    @Transactional
    public ProductTypeResponse updateProductType(Long id, ProductTypeRequest request, Jwt jwt) {
        String email = jwt.getClaimAsString("iss");

        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Type not found"));

        // Security Check: Does this type belong to the user?
        if (!productType.getUser().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this type");
        }

        productType.setName(request.name());
        productType.setStatus(request.status() != null ? request.status() : productType.getStatus());

        productTypeRepository.save(productType);

        return new ProductTypeResponse(
                productType.getId(),
                productType.getName(),
                productType.getStatus(),
                productType.getCreatedAt(),
                productType.getUser().getName()
        );
    }

    @Override
    public void deleteProductType(Long id, Jwt jwt) {
        String email = jwt.getClaimAsString("iss");

        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Type not found"));

        // Security Check
        if (!productType.getUser().getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this type");
        }

        productTypeRepository.delete(productType);
    }
}
