package invoice.com.demo.notification.listener;

import invoice.com.demo.notification.NotificationChannel;
import invoice.com.demo.notification.entity.Notification;
import invoice.com.demo.notification.event.NotificationEvent;
import invoice.com.demo.notification.repository.NotificationRepository;
import invoice.com.demo.notification.service.TelegramNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationRepository notificationRepository;
    private final TelegramNotificationService telegramNotificationService;

    @Async
    @EventListener
    public void handleNotification(NotificationEvent event) {
        List<NotificationChannel> channels = event.channels();
        if (channels == null || channels.isEmpty()) {
            return;
        }

        if (channels.contains(NotificationChannel.SYSTEM)) {
            Notification notification = new Notification();
            notification.setUserId(event.userId());
            notification.setTitle(event.title());
            notification.setMessage(event.message());
            notification.setReadStatus(false);
            notificationRepository.save(notification);
        }

        if (channels.contains(NotificationChannel.TELEGRAM)) {
            telegramNotificationService.send(event.userId(), event.title(), event.message());
        }
    }
}
