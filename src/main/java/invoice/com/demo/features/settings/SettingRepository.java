package invoice.com.demo.features.settings;

import invoice.com.demo.domain.Setting;
import invoice.com.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting>findByUser(User user);
}
