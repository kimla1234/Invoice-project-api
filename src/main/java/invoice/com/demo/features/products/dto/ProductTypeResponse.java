package invoice.com.demo.features.products.dto;

import java.time.LocalDateTime;

public record ProductTypeResponse(
        Long id,
        String name,
        Boolean status,

        // Audit information from Auditable
        LocalDateTime createdAt,

        // Identity of the creator (optional, or just the name/UUID)
        String createdBy
) {
}