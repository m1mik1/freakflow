package com.freakflow.backend.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Групуємо ендпоінти під «public» API
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("freakflow-public")
                .pathsToMatch("/api/**")
                .build();
    }

    // Основна мета інформація
    @Bean
    public OpenAPI freakflowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FreakFlow Q&A API")
                        .version("v1")
                        .description("REST API для Q&A-системи FreakFlow"));
    }
}
