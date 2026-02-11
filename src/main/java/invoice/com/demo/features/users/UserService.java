package invoice.com.demo.features.users;

import invoice.com.demo.features.users.dto.UpdateUserRequest;
import invoice.com.demo.features.users.dto.UserResponse;
import org.springframework.security.oauth2.jwt.Jwt;


public interface UserService {
    UserResponse findOwnProfile(Jwt jwt);
    UserResponse updateProfile(Jwt jwt, UpdateUserRequest request);
}
