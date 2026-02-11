package invoice.com.demo.notification.repository;

import invoice.com.demo.notification.entity.NotificationRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, Long> {
    Optional<NotificationRecipient> findByUserId(Long userId);
}
