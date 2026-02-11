package invoice.com.demo.notification.service;

import invoice.com.demo.notification.NotificationChannel;
import invoice.com.demo.notification.entity.Notification;
import invoice.com.demo.notification.event.NotificationEvent;
import invoice.com.demo.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationRepository notificationRepository;

    public void notify(Long userId, String title, String message, List<NotificationChannel> channels) {
        List<NotificationChannel> resolvedChannels = (channels == null || channels.isEmpty())
                ? List.of(NotificationChannel.SYSTEM)
                : List.copyOf(channels);
        eventPublisher.publishEvent(new NotificationEvent(userId, title, message, resolvedChannels));
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }
}
