package invoice.com.demo.features.auth;

import invoice.com.demo.base.BaseMessage;
import invoice.com.demo.domain.Role;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.auth.dto.AuthResponse;
import invoice.com.demo.features.auth.dto.LoginRequest;
import invoice.com.demo.features.auth.dto.RefreshTokenRequest;
import invoice.com.demo.features.auth.dto.RegisterRequest;
import invoice.com.demo.features.token.AuthTokenService;
import invoice.com.demo.features.token.TokenRepository;
import invoice.com.demo.features.users.RoleRepository;
import invoice.com.demo.features.users.UserRepository;
import invoice.com.demo.mapper.AuthMapper;
import invoice.com.demo.utils.PasswordValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import invoice.com.demo.features.settings.SettingRepository;
import invoice.com.demo.domain.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final AuthTokenService authTokenService;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final SettingRepository settingRepository;
    private final RoleRepository roleRepository;



    @Transactional
    @Override
    public BaseMessage register(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password and Confirm Password must be same"
            );
        }

        if (!PasswordValidator.validate(registerRequest.password())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character"
            );
        }

        User user = authMapper.mapFromRegisterCreateRequest(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setUuid(UUID.randomUUID().toString());
        user.setIsDelete(false);
        user.setIsVerified(false);

        // set default role USER when create user
        List<Role> roleList = new ArrayList<>();
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User role does not exist!"
                ));

        roleList.add(role);
        user.setRoles(roleList);

        User savedUser = userRepository.save(user);


        // 🔑 CREATE SETTING HERE
        Setting setting = new Setting();
        setting.setUser(savedUser);
        settingRepository.save(setting);
       // userRepository.save(user);
       //userRepository.flush();


        return BaseMessage.builder()
                .message("Registered Successfully")
                .build();
    }

    @Override
    public AuthResponse userLogin(LoginRequest loginRequest) {
        if (!userRepository.existsByEmail(loginRequest.email())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User has not been found!"
            );
        }

        User user = userRepository.findByEmailAndIsDeleteFalse(loginRequest.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User has not been found!"
                ));


        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        Authentication authentication = daoAuthenticationProvider.authenticate(auth);
        // DEBUG: Print to console to see if tokens exist before returning
        AuthResponse response = authTokenService.createToken(authentication);
        log.info("Access Token: {}", response);

        return response;
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return authTokenService.refreshToken(refreshTokenRequest);
    }

}
