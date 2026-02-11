package invoice.com.demo.notification.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "telegram")
@Getter
@Setter
public class TelegramProperties {
    private boolean enabled = false;
    private Bot bot = new Bot();

    @Getter
    @Setter
    public static class Bot {
        private String token;
    }
}
