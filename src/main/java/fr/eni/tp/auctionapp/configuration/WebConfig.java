package fr.eni.tp.auctionapp.configuration;

import fr.eni.tp.auctionapp.interceptor.UserRefreshInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserRefreshInterceptor userRefreshInterceptor;

    public WebConfig(UserRefreshInterceptor userRefreshInterceptor) {
        this.userRefreshInterceptor = userRefreshInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userRefreshInterceptor);
    }
}