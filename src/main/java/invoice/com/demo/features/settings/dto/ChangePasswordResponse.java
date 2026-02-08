package invoice.com.demo.features.settings.dto;

public record ChangePasswordResponse(
        Long userId,
        String message
) {
}
