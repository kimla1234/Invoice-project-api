package invoice.com.demo.features.token;

import invoice.com.demo.base.BaseMessage;
import invoice.com.demo.domain.Token;
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
    private  final userMapper userMapper;
    private final String TOKEN_TYPE = "Bearer";
    @Autowired
    @Qualifier("refreshJwtEncoder")
    private JwtEncoder refreshJwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;



    @Override
    public AuthResponse createToken(Authentication authentication) {

        // Extract email from authenticated principal
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User with email " + email + " not found"
                ));

        return new AuthResponse(
                TOKEN_TYPE,
                createAccessToken(authentication),
                createRefreshToken(authentication),
                userMapper.mapFromUserToUserResponse(user)

        );
    }

    @Override
    public String createAccessToken(Authentication authentication) {
        String scope = "";

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            scope = jwt.getClaimAsString("scope");
        } else {
            scope = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    //.filter(authority -> !authority.startsWith("ROLE_"))
                    .collect(Collectors.joining(" "));
        }

        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(authentication.getName())
                .issuer(authentication.getName())
                .subject(authentication.getName())
                .audience(List.of("WEB,MOBILE"))
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
    }

    @Override
    public String createRefreshToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope;

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            scope = jwt.getClaimAsString("scope");
        } else {
            scope = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> !authority.startsWith("ROLE_"))
                    .collect(Collectors.joining(" "));
        }

        JwtClaimsSet refreshJwtClaimsSet = JwtClaimsSet.builder()
                .id(authentication.getName())
                .subject("Refresh Resource")
                .audience(List.of("WEB", "MOBILE"))
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .issuer(authentication.getName())
                .claim("scope", scope)
                .build();

        return refreshJwtEncoder.encode(JwtEncoderParameters.from(refreshJwtClaimsSet)).getTokenValue();
    }


    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        Authentication authentication = new BearerTokenAuthenticationToken(refreshTokenRequest.refreshToken());
        authentication = jwtAuthenticationProvider.authenticate(authentication);

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            // Extract claims from the refresh token
            String email = jwt.getClaimAsString("iss");
            String scope = jwt.getClaimAsString("scope");

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "User with email " + email + " not found"
                    ));

            Instant now = Instant.now();

            // Create new JWT claims for the access token
            JwtClaimsSet accessJwtClaimsSet = JwtClaimsSet.builder()
                    .id(email)
                    .issuer(email)
                    .subject(email)
                    .audience(List.of("WEB", "MOBILE"))
                    .issuedAt(now)
                    .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                    .claim("scope", scope)
                    .build();

            // Create new JWT claims for the refresh token
            JwtClaimsSet refreshJwtClaimsSet = JwtClaimsSet.builder()
                    .id(email)
                    .issuer(email)
                    .subject("Refresh Resource")
                    .audience(List.of("WEB", "MOBILE"))
                    .issuedAt(now)
                    .expiresAt(now.plus(1, ChronoUnit.DAYS))
                    .claim("scope", scope)
                    .build();

            String newAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessJwtClaimsSet)).getTokenValue();
            String newRefreshToken = refreshJwtEncoder.encode(JwtEncoderParameters.from(refreshJwtClaimsSet)).getTokenValue();

            return new AuthResponse("Bearer",newAccessToken, newRefreshToken, userMapper.mapFromUserToUserResponse(user));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }

}
