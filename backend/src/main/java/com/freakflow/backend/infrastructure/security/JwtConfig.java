package com.freakflow.backend.infrastructure.security;

import com.freakflow.backend.domain.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;      // длинная, случайная строка
    private long   expiration;
}

