package invoice.com.demo.notification.event;

import invoice.com.demo.notification.NotificationChannel;

import java.util.List;

public record NotificationEvent(
        Long userId,
        String title,
        String message,
        List<NotificationChannel> channels
) {
}
