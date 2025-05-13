package com.freakflow.backend.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Группа публичных API как у вас
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("freakflow-public")
                .pathsToMatch("/api/**")
                .build();
    }

    // Основной бин OpenAPI с security-схемой
    @Bean
    public OpenAPI freakflowOpenAPI() {
        return new OpenAPI()
                // Общая информация о вашем API
                .info(new Info()
                        .title("FreakFlow Q&A API")
                        .version("v1")
                        .description("REST API для Q&A-системи FreakFlow"))
                // Объявляем, что у нас есть схема авторизации bearerAuth
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Введите JWT в формате **Bearer &lt;token&gt;**")
                        )
                )
                // Говорим, что по умолчанию все эндпоинты требуют этой схемы
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
