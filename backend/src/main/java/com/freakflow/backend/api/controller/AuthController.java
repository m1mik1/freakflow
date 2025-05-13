package com.freakflow.backend.api.controller;

import com.freakflow.backend.application.dto.request.AuthLoginRequest;
import com.freakflow.backend.application.dto.request.AuthRegisterRequest;
import com.freakflow.backend.application.dto.request.VerificationRequest;
import com.freakflow.backend.application.dto.request.VerificationResendRequest;
import com.freakflow.backend.application.dto.response.AuthLoginResponse;
import com.freakflow.backend.application.dto.response.AuthRegisterResponse;
import com.freakflow.backend.application.dto.response.UserResponse;
import com.freakflow.backend.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthRegisterResponse register(@Valid @RequestBody AuthRegisterRequest dto) {
       return authService.register(dto);
    }

    @PostMapping("/verify")
    public UserResponse verify(@Valid @RequestBody VerificationRequest dto) {
        return authService.verify(dto);
    }

    @PostMapping("/verify/resend")
    public ResponseEntity<Void> resend(@Valid @RequestBody VerificationResendRequest dto) {
        authService.resendCode(dto.email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public AuthLoginResponse login(@Valid @RequestBody AuthLoginRequest dto) {
        return authService.login(dto);
    }

}
