package invoice.com.demo.features.token;

import invoice.com.demo.domain.User;
import invoice.com.demo.features.auth.dto.AuthResponse;
import invoice.com.demo.features.auth.dto.RefreshTokenRequest;
import invoice.com.demo.features.users.UserRepository;
import invoice.com.demo.mapper.userMapper;
import invoice.com.demo.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private final userMapper userMapper;
    private final String TOKEN_TYPE = "Bearer";

    @Autowired
    @Qualifier("refreshJwtEncoder")
    private JwtEncoder refreshJwtEncoder;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    // --- Helper Method getEmail ---
    private String getEmailFromAuth(Authentication authentication) {
        if (authentication.getPrincipal() instanceof CustomUserDetail userDetail) {
            return userDetail.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("email");
            if (email == null) email = jwt.getClaimAsString("iss");
            return email;
        } else if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User oAuth2User) {
            return oAuth2User.getAttribute("email");
        }
        return authentication.getName();
    }

    // --- Helper Method Scope ---
    private String extractScope(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getClaimAsString("scope");
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    @Override
    public AuthResponse createToken(Authentication authentication) {
        String identity = getEmailFromAuth(authentication);

        User user = userRepository.findByEmail(identity)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with email " + identity + " not found"
                ));

        return new AuthResponse(
                TOKEN_TYPE,
                createAccessToken(user.getEmail(), authentication),
                createRefreshToken(authentication),
                userMapper.mapFromUserToUserResponse(user)
        );
    }

    @Override
    public String createAccessToken(String email, Authentication authentication) {
        String scope = extractScope(authentication);
        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(email)
                .issuer(email)
                .subject(email)
                .audience(List.of("WEB", "MOBILE"))
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.MINUTES)) // បង្កើនទៅ ១៥ នាទី
                .claim("scope", scope)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    @Override
    public String createRefreshToken(Authentication authentication) {
        String email = getEmailFromAuth(authentication);
        String scope = extractScope(authentication);
        Instant now = Instant.now();

        JwtClaimsSet refreshJwtClaimsSet = JwtClaimsSet.builder()
                .id(email)
                .issuer(email)
                .subject("Refresh Resource")
                .audience(List.of("WEB", "MOBILE"))
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .claim("scope", scope)
                .build();

        return refreshJwtEncoder.encode(JwtEncoderParameters.from(refreshJwtClaimsSet)).getTokenValue();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Authentication authentication = new BearerTokenAuthenticationToken(refreshTokenRequest.refreshToken());

        try {
            authentication = jwtAuthenticationProvider.authenticate(authentication);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token");
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String email = jwt.getClaimAsString("iss");
            String scope = jwt.getClaimAsString("scope");

            log.info("Refreshing token for user: {}", email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User with email " + email + " not found"
                    ));

            Instant now = Instant.now();

            String newAccessToken = createAccessToken(user.getEmail(), authentication);

            JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
                    .id(email).issuer(email).subject("Refresh Resource")
                    .audience(List.of("WEB", "MOBILE"))
                    .issuedAt(now).expiresAt(now.plus(7, ChronoUnit.DAYS))
                    .claim("scope", scope).build();

            String newRefreshToken = refreshJwtEncoder.encode(JwtEncoderParameters.from(refreshClaims)).getTokenValue();

            return new AuthResponse(TOKEN_TYPE, newAccessToken, newRefreshToken, userMapper.mapFromUserToUserResponse(user));
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid principal type");
    }
}