package invoice.com.demo.features.clients.dto;

import invoice.com.demo.enumeration.Gender;
import invoice.com.demo.features.users.dto.UserResponse;

import java.time.LocalDateTime;

public record ClientResponse (
        Long id,
        String name,
        Gender gender,
        String phoneNumber,
        String address,
        LocalDateTime createAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
        )
{
}
