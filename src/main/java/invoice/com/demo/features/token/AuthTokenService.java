package invoice.com.demo.features.token;


import invoice.com.demo.features.auth.dto.AuthResponse;
import invoice.com.demo.features.auth.dto.RefreshTokenRequest;
import org.springframework.security.core.Authentication;

public interface AuthTokenService {
    AuthResponse createToken(Authentication authentication);

    String createAccessToken(Authentication authentication);

    String createRefreshToken(Authentication authentication);

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
