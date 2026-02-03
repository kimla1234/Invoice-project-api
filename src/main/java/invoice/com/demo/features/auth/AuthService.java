package invoice.com.demo.features.auth;

import invoice.com.demo.base.BaseMessage;
import invoice.com.demo.features.auth.dto.AuthResponse;
import invoice.com.demo.features.auth.dto.LoginRequest;
import invoice.com.demo.features.auth.dto.RefreshTokenRequest;
import invoice.com.demo.features.auth.dto.RegisterRequest;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    BaseMessage register(RegisterRequest registerRequest) throws MessagingException;
    AuthResponse userLogin(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
