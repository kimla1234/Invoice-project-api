package invoice.com.demo.security.oauth2;

import invoice.com.demo.domain.Setting;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.settings.SettingRepository;
import invoice.com.demo.features.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final SettingRepository settingRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setUuid(UUID.randomUUID().toString());
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setImage_profile(picture);
            newUser.setIsVerified(true);
            newUser.setIsDelete(false);
            newUser.setStatus(true);

            User savedUser = userRepository.save(newUser);

            Setting setting = new Setting();
            setting.setUser(savedUser);
            settingRepository.save(setting);

            return savedUser;
        });

        return oAuth2User;
    }
}