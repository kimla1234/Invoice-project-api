package invoice.com.demo.features.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductTypeRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be under 100 characters")
        String name,

        Boolean status // Optional: defaults to true in the entity
) {
}
