package invoice.com.demo.features.settings;
import invoice.com.demo.domain.Setting;
import invoice.com.demo.domain.User;
import invoice.com.demo.features.settings.SettingRepository;
import invoice.com.demo.features.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SettingInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SettingRepository settingRepository;

    @Override
    public void run(String... args) {

        List<User> users = userRepository.findAll();

        for (User user : users) {
            boolean exists = settingRepository.findByUser(user).isPresent();

            if (!exists) {
                Setting setting = new Setting();
                setting.setUser(user);
                settingRepository.save(setting);
            }
        }
    }
}
