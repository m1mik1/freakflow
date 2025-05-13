package com.freakflow.backend.application.service;

import com.freakflow.backend.application.dto.request.AuthLoginRequest;
import com.freakflow.backend.application.dto.request.AuthRegisterRequest;
import com.freakflow.backend.application.dto.request.VerificationRequest;
import com.freakflow.backend.application.dto.response.AuthLoginResponse;
import com.freakflow.backend.application.dto.response.AuthRegisterResponse;
import com.freakflow.backend.application.dto.response.UserResponse;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.UserRepository;
import com.freakflow.backend.domain.valueobject.EmailAddress;
import com.freakflow.backend.infrastructure.security.JwtConfig;
import com.freakflow.backend.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;
    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;

    @Transactional
    public AuthRegisterResponse register(AuthRegisterRequest authRegisterRequest) {
        EmailAddress email = new EmailAddress(authRegisterRequest.email);
        Optional<User> existing = userRepository.findByEmail(email);

        if (existing.isPresent()) {
            User u = existing.get();


            if (!u.isEnabled()) {
                u.setName(authRegisterRequest.name);
                u.setPasswordHash(passwordEncoder.encode(authRegisterRequest.password));
                userRepository.save(u);
                verificationService.resendCode(authRegisterRequest.email);

                AuthRegisterResponse authRegisterResponse = new AuthRegisterResponse();
                authRegisterResponse.email= authRegisterRequest.email;
                return authRegisterResponse;
            }
            throw new IllegalArgumentException("Email already exists");

        }
        if (userRepository.existsByName(authRegisterRequest.name)) {
            throw new IllegalArgumentException("Name already exists");
        }


        if (!Objects.equals(authRegisterRequest.password, authRegisterRequest.confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        User user = new User();
        user.setEmail(email);
        user.setName(authRegisterRequest.name);
        user.setPasswordHash(passwordEncoder.encode(authRegisterRequest.password));

        log.info("User with name: {} created. ", authRegisterRequest.name);
        userRepository.save(user);

        verificationService.sendVerificationCode(user);

        AuthRegisterResponse authRegisterResponse = new AuthRegisterResponse();
        authRegisterResponse.email= authRegisterRequest.email;
        return authRegisterResponse;

    }

    @Transactional
    public UserResponse verify(VerificationRequest dto) {
        verificationService.verifyCode(dto.email, dto.code);

        EmailAddress email = new EmailAddress(dto.email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found."));

        UserResponse userResponse = new UserResponse();
        userResponse.id = user.getId();
        userResponse.name = user.getName();
        userResponse.email = dto.email;
        return userResponse;
    }

    @Transactional(readOnly = true)
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {
        EmailAddress email = new EmailAddress(authLoginRequest.email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(authLoginRequest.password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (!user.isEnabled()) {
            throw new IllegalStateException("E-mail not verified");
        }

        String token = jwtUtil.generateAccessToken(user);
        long expiresIn = jwtConfig.getExpiration() / 1000;

        AuthLoginResponse authLoginResponse = new AuthLoginResponse();
        authLoginResponse.token = token;
        authLoginResponse.tokenType = "Bearer";
        authLoginResponse.expiresIn=expiresIn;
        authLoginResponse.userId = user.getId();
        authLoginResponse.name = user.getName();

        return authLoginResponse;

    }

    @Transactional
    public void resendCode(String email) {
        verificationService.resendCode(email);
    }

}

