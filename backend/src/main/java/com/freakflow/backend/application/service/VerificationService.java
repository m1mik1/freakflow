package com.freakflow.backend.application.service;

import com.freakflow.backend.domain.model.EmailVerificationToken;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.VerificationTokenRepository;
import com.freakflow.backend.domain.valueobject.EmailAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {
    private final VerificationTokenRepository tokenRepo;
    private final JavaMailSender mailSender;

    private String generateCode() {
        String code;
        do {
            code = RandomStringUtils.randomAlphanumeric(6);
            code = code.toUpperCase();
        } while (!code.matches(".*\\d.*") || !code.matches(".*[A-Z].*"));
        return code;
    }

    @Transactional
    public void sendVerificationCode(User user) {
        String code = generateCode();
        Instant expires = Instant.now().plus(20, ChronoUnit.MINUTES);

        var token = EmailVerificationToken.builder()
                .user(user)
                .code(code)
                .createdAt(Instant.now())
                .expiresAt(expires)
                .build();
        tokenRepo.save(token);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(user.getEmail().getValue());
        msg.setSubject("Ваш код подтверждения");
        msg.setText("Ваш код: " + code);
        mailSender.send(msg);

        log.info("Verification code for {}: {}", user.getEmail().getValue(), code);
    }

    @Transactional
    public void verifyCode(String emailstr, String code) {
        EmailAddress email = new EmailAddress(emailstr);
        var optToken = tokenRepo
                .findByUserEmailAndCode(email, code)
                .filter(t -> t.getExpiresAt().isAfter(Instant.now()));

        var token = optToken.orElseThrow(() ->
                new IllegalArgumentException("Невірний чи прострочений код."));

        // помечаем пользователя подтверждённым
        User user = token.getUser();
        user.markEmailVerified();

        // удаляем старые токены
        tokenRepo.deleteByUser(user);
    }
}

