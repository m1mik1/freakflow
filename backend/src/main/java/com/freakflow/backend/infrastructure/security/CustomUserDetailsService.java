package com.freakflow.backend.infrastructure.security;


import com.freakflow.backend.domain.model.User;
import com.freakflow.backend.domain.repository.UserRepository;
import com.freakflow.backend.domain.valueobject.EmailAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(new EmailAddress(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return toCustomDetails(user);
    }

    // Новый метод: из id → UserDetails (это для JWT-фильтра)
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("ID " + id));
        return toCustomDetails(user);
    }

    private CustomUserDetails toCustomDetails(User user) {
        // здесь можете грузить реальные роли, если надо
        List<GrantedAuthority> auths = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new CustomUserDetails(
                user.getId(),
                user.getEmail().getValue(),
                user.getPasswordHash(),
                auths
        );
    }
}

