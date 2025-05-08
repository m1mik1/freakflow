package com.freakflow.backend.domain.model;

import com.freakflow.backend.domain.exception.InvalidAssociationException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name="comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "question_id", nullable = true)
    private Question question;

    @ManyToOne(fetch = LAZY, optional = true)
    @JoinColumn(name = "answer_id",   nullable = true)
    private Answer   answer;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist @PreUpdate
    private void beforeAnySave() {
        validateParent();
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        updatedAt = Instant.now();
    }


    private void validateParent() {
        if ((question == null) == (answer == null)) {
            throw new InvalidAssociationException(
                    "Comment must relate to exactly one of Question or Answer"
            );
        }
    }
}
