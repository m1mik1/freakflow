package com.freakflow.backend.infrastructure.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/api/**")                            // все ваши REST-эндпоинты
                .allowedOrigins("http://localhost:3000")         // источник, откуда идут запросы
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // статика из classpath (default-avatar.png)
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // загруженные аватарки из внешней папки
        registry
                .addResourceHandler("/uploads/avatars/**")
                .addResourceLocations("file:/app/uploads/avatars/");
    }
}

