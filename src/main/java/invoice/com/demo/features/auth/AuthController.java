package invoice.com.demo.features.auth;

import invoice.com.demo.base.BaseMessage;
import invoice.com.demo.features.auth.dto.AuthResponse;
import invoice.com.demo.features.auth.dto.LoginRequest;
import invoice.com.demo.features.auth.dto.RefreshTokenRequest;
import invoice.com.demo.features.auth.dto.RegisterRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseMessage register(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {

        return authService.register(registerRequest);
    }

    @PostMapping("/user/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse userLogin(@Valid @RequestBody LoginRequest loginRequest) {

        return authService.userLogin(loginRequest);
    }

    @PostMapping("/refresh-token")
    public AuthResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {

        return authService.refreshToken(refreshTokenRequest);
    }

}
