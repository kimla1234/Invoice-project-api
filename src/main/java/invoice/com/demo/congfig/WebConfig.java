package invoice.com.demo.congfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${media.server-path}")
    private String serverPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ensure the path ends with a slash and starts with 'file:'
        String location = serverPath.endsWith("/") ? "file:" + serverPath : "file:" + serverPath + "/";
        registry.addResourceHandler("/media/**")
                .addResourceLocations(location);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
