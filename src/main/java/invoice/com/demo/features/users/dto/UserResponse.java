package invoice.com.demo.features.users.dto;

public record UserResponse(
        Long id ,
        String uuid,
        String name,
        String email,
        String phone_number,
        String  image_profile

) {
}
