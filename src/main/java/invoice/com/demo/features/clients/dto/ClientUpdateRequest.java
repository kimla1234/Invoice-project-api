package invoice.com.demo.features.clients.dto;

import invoice.com.demo.enumeration.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientUpdateRequest(
        @NotBlank(message = "Client name is required")
        String name,

        @NotNull(message = "Gender is required")
        Gender gender,
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @NotNull(message = "address is required")
        String address
) {
}
