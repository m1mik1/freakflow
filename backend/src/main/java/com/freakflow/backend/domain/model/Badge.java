package com.freakflow.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "badges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Уникальное имя бейджа */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Описание бейджа */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    /** Критерии получения бейджа */
    @Column(length = 255)
    private String criteria;

    /** Пользователи, получившие этот бейдж */
    @ManyToMany(mappedBy = "badges", fetch = LAZY)
    @Builder.Default
    private Set<User> users = new HashSet<>();
}
