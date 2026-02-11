package invoice.com.demo.features.users.dto;

public record UpdateUserRequest(
        String name ,
        String phone_number,
        String image_profile
) {
}
