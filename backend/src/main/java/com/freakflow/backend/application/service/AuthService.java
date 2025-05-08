package com.freakflow.backend.application.service;

import com.freakflow.backend.application.dto.RegisterRequestDto;
import com.freakflow.backend.application.dto.UserDto;
import com.freakflow.backend.application.dto.VerificationRequestDto;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.UserRepository;
import com.freakflow.backend.domain.valueobject.EmailAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;

    @Transactional
    public void register(RegisterRequestDto registerRequestDto) {
        EmailAddress email = new EmailAddress(registerRequestDto.email);
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (userRepository.existsByName(registerRequestDto.name)) {
            throw new IllegalArgumentException("Name already exists.");
        }
        if (!Objects.equals(registerRequestDto.password, registerRequestDto.confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        User user = new User();
        user.setEmail(email);
        user.setName(registerRequestDto.name);
        user.setPasswordHash(passwordEncoder.encode(registerRequestDto.password));

        log.info("User with name: {} created. ", registerRequestDto.name);
        userRepository.save(user);

        verificationService.sendVerificationCode(user);

    }

    public UserDto verify(VerificationRequestDto dto) {
        verificationService.verifyCode(dto.email, dto.code);

        EmailAddress email = new EmailAddress(dto.email);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found."));

        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.name = user.getName();
        userDto.email = dto.email;
        return userDto;
    }

}

