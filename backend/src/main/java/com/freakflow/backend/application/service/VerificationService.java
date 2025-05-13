package com.freakflow.backend.application.service;

import com.freakflow.backend.domain.model.EmailVerificationToken;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.UserRepository;
import com.freakflow.backend.domain.repository.VerificationTokenRepository;
import com.freakflow.backend.domain.valueobject.EmailAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {
    private final VerificationTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

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

        tokenRepository.deleteByUser(user);

        String code = generateCode();
        Instant expires = Instant.now().plus(20, ChronoUnit.MINUTES);

        var token = EmailVerificationToken.builder()
                .user(user)
                .code(code)
                .createdAt(Instant.now())
                .expiresAt(expires)
                .build();
        tokenRepository.save(token);

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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        var token = tokenRepository.findByUserAndCode(user, code)
                .filter(t -> t.getExpiresAt().isAfter(Instant.now()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Невірний чи прострочений код.")
                );

        user.setEnabled(true);
        tokenRepository.deleteByUser(user);

    }

    @Transactional
    public void resendCode(String emailstr) {
        EmailAddress email = new EmailAddress(emailstr);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->  new IllegalArgumentException("User not found"));
        if (user.isEnabled()){
            throw new IllegalStateException("Already verified");
        }
        tokenRepository.deleteByUser(user);
        sendVerificationCode(user);
    }
}

