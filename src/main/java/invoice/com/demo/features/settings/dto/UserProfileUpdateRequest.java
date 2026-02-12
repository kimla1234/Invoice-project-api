package invoice.com.demo.features.settings.dto;

public record UserProfileUpdateRequest(
        String name,
        String phoneNumber,
        String imageProfile
) {
}
