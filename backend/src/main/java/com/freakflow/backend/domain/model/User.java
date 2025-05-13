package com.freakflow.backend.domain.model;

import com.freakflow.backend.domain.valueobject.EmailAddress;
import com.freakflow.backend.domain.valueobject.Reputation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.Instant;
import java.util.*;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
              @AttributeOverride(name="value", column=@Column(name="email", nullable=false, unique=true, length=100))
    })
    private EmailAddress email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name ="value", column = @Column(name = "reputation",nullable = false))})
    private Reputation reputation= new Reputation(0);

    @ManyToMany(fetch = LAZY, cascade = { PERSIST, MERGE })
    @JoinTable(
            name = "user_badge",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "badge_id")
    )
    @Builder.Default
    private Set<Badge> badges = new HashSet<>();

    @OneToMany(
            mappedBy = "author",
            fetch     = LAZY,
            cascade   = ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Question> questions = new ArrayList<>();

    @OneToMany(
            mappedBy = "author",
            fetch     = LAZY,
            cascade   = ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(
            mappedBy = "author",
            fetch     = LAZY,
            cascade   = ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
            mappedBy = "author",
            fetch     = LAZY,
            cascade   = ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Vote> votes = new ArrayList<>();


    @OneToMany(
            mappedBy = "user",
            fetch     = LAZY,
            cascade   = ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    private void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
