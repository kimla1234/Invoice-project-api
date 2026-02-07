package invoice.com.demo.features.users;

import invoice.com.demo.domain.User;
import invoice.com.demo.features.users.dto.UserResponse;
import invoice.com.demo.mapper.userMapper;
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
}
