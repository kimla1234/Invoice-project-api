package invoice.com.demo.features.users;

import invoice.com.demo.domain.Setting;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.settings.SettingRepository;
import invoice.com.demo.features.users.dto.UpdateUserRequest;
import invoice.com.demo.features.users.dto.UserResponse;
import invoice.com.demo.mapper.userMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final userMapper userMapper ;

    @Override
    public UserResponse findOwnProfile(Jwt jwt) {

        if (jwt == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized"
            );
        }

        String email = jwt.getClaimAsString("iss");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return userMapper.mapFromUserToUserResponse(user);
    }

    @Override
    public UserResponse updateProfile(Jwt jwt, UpdateUserRequest request) {
        String email = jwt.getClaim("iss");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "រកមិនឃើញអ្នកប្រើប្រាស់នេះឡើយ"));

        if (request.name() != null) {
            user.setName(request.name());
        }

        if (request.phone_number() != null) {
            user.setPhone_number(request.phone_number());
        }

        if (request.image_profile() != null) {
            user.setImage_profile(request.image_profile());
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }
}
