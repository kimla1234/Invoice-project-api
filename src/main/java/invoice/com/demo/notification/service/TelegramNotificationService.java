package invoice.com.demo.notification.service;

import invoice.com.demo.notification.config.TelegramProperties;
import invoice.com.demo.notification.entity.NotificationRecipient;
import invoice.com.demo.notification.repository.NotificationRecipientRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(TelegramNotificationService.class);

    private final TelegramProperties telegramProperties;
    private final NotificationRecipientRepository recipientRepository;
    private final RestTemplate restTemplate;

    public void send(Long userId, String title, String message) {
        if (!telegramProperties.isEnabled()) {
            return;
        }

        String token = telegramProperties.getBot().getToken();
        if (!StringUtils.hasText(token)) {
            logger.warn("Telegram is enabled but bot token is missing");
            return;
        }

        Optional<NotificationRecipient> recipient = recipientRepository.findByUserId(userId);
        if (recipient.isEmpty() || !StringUtils.hasText(recipient.get().getTelegramChatId())) {
            logger.info("Telegram chat id is not configured for userId={}", userId);
            return;
        }

        String text = title + "\n" + message;
        String url = "https://api.telegram.org/bot" + token + "/sendMessage";

        Map<String, Object> payload = new HashMap<>();
        payload.put("chat_id", recipient.get().getTelegramChatId());
        payload.put("text", text);

        try {
            restTemplate.postForEntity(url, payload, String.class);
        } catch (Exception ex) {
            logger.warn("Failed to send Telegram notification for userId={}", userId, ex);
        }
    }
}
