package com.freakflow.backend.api.controller;

import com.freakflow.backend.api.GlobalExceptionHandler;
import com.freakflow.backend.application.dto.RegisterRequestDto;
import com.freakflow.backend.application.dto.UserDto;
import com.freakflow.backend.application.dto.VerificationRequestDto;
import com.freakflow.backend.application.service.AuthService;
import com.freakflow.backend.application.service.VerificationService;
import com.freakflow.backend.domain.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final VerificationService verificationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequestDto dto) {
        authService.register(dto);
    }

    @PostMapping("/verify")
    public UserDto verify(@Valid @RequestBody VerificationRequestDto dto) {
        return authService.verify(dto);
    }

}
