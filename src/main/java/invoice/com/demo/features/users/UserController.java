package invoice.com.demo.features.users;

import invoice.com.demo.base.BaseResponse;
import invoice.com.demo.features.users.dto.UpdateUserRequest;
import invoice.com.demo.features.users.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserResponse findOwnProfile(@AuthenticationPrincipal Jwt jwt) {
        return userService.findOwnProfile(jwt);
    }

    @PatchMapping("/me")
    public BaseResponse<Object> updateOwnProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        // ក្នុង Service វានឹងទាញយក User តាមរយៈ Email ក្នុង JWT ស្រាប់
        UserResponse updatedUser = userService.updateProfile(jwt, request);

        return BaseResponse.<UserResponse>builder()
                .payload(updatedUser)
                .build();
    }
}
