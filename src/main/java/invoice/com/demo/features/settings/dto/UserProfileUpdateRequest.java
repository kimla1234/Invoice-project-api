package invoice.com.demo.features.settings.dto;

public record UserProfileUpdateRequest(
        String name,
        String phone_number,
        String image_profile
) {
}
