package invoice.com.demo.features.auth.dto;


import invoice.com.demo.features.users.dto.UserResponse;

public record AuthResponse(
        String type,
        String accessToken,
        String refreshToken,
        UserResponse user
) {
}
