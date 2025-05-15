package com.freakflow.backend.api.controller;

import com.freakflow.backend.application.dto.request.UserAvatarUpdateRequest;
import com.freakflow.backend.application.dto.request.UserUpdateRequest;
import com.freakflow.backend.application.dto.response.UserResponse;
import com.freakflow.backend.application.service.UserService;
import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserResponse getAllUsers() {
        return null;
    }
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody UserUpdateRequest dto) {
        UserResponse updated= userService.updateProfile(currentUser.getId(),dto);
        return ResponseEntity.ok(updated);
    }
    @PatchMapping(
            path = "/me/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> updateAvatar(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestPart("file") MultipartFile file
    ) {
        userService.updateAvatar(principal.getId(), file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        userService.deleteUserById(currentUser.getId());
        return ResponseEntity.noContent().build();
    }


}
