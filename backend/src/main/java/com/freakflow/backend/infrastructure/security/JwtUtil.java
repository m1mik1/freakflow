package com.freakflow.backend.infrastructure.security;

import com.freakflow.backend.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig cfg;

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + cfg.getExpiration());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("name",  user.getName())
                .claim("email", user.getEmail().getValue())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, cfg.getSecret().getBytes())
                .compact();
    }

    public Long parseUserId(String token) {
        Claims c = Jwts.parser()
                .setSigningKey(cfg.getSecret().getBytes())
                .parseClaimsJws(token)
                .getBody();
        return Long.valueOf(c.getSubject());
    }
}

