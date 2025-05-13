package com.freakflow.backend;

import com.freakflow.backend.infrastructure.security.JwtConfig;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "Your API", version = "v1", description = "Документация API"),
        tags = {
        }
)
@EnableConfigurationProperties(JwtConfig.class)
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
