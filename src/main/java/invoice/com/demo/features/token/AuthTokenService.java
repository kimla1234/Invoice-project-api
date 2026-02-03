package invoice.com.demo.features.token;

import invoice.com.demo.base.BaseMessage;
import invoice.com.demo.domain.Token;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.auth.dto.AuthResponse;
import invoice.com.demo.features.auth.dto.RefreshTokenRequest;
import org.springframework.security.core.Authentication;

public interface AuthTokenService {
    AuthResponse createToken(Authentication authentication);

    String createAccessToken(Authentication authentication);

    String createRefreshToken(Authentication authentication);

    String generateResetToken(String email, User user, Token token);

    BaseMessage resetPassword(String token, String newPassword);

    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
