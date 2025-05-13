package com.freakflow.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationToken {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 6)
    private String code;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id",unique = true, nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        expiresAt = createdAt.plus(5, ChronoUnit.MINUTES);
    }
}
