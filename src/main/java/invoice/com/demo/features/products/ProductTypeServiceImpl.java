package invoice.com.demo.features.products;

import invoice.com.demo.domain.ProductType;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.products.dto.ProductTypeRequest;
import invoice.com.demo.features.products.dto.ProductTypeResponse;
import invoice.com.demo.features.users.UserRepository;
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
        // Extract user from JWT (assuming 'sub' or a custom claim 'id' holds the UUID)
        String email = jwt.getClaimAsString("iss");


        String userUuid = jwt.getClaimAsString("email");
        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductType productType = new ProductType();
        productType.setName(request.name());
        productType.setStatus(request.status() != null ? request.status() : true);
        productType.setUser(creator);

        productTypeRepository.save(productType);

        // Return the response DTO
        return new ProductTypeResponse(
                productType.getId(),
                productType.getName(),
                productType.getStatus(),
                productType.getCreatedAt(),
                creator.getName()
        );
    }

    @Override
    public List<ProductTypeResponse> getMyProductType(Jwt jwt) {
        return List.of();
    }
}
