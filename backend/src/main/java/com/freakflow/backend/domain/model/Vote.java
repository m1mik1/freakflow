package com.freakflow.backend.domain.model;

import com.freakflow.backend.domain.exception.InvalidAssociationException;
import com.freakflow.backend.domain.valueobject.VoteValue;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(name = "uk_vote_author_question", columnNames = {"author_id", "question_id"}), @UniqueConstraint(name = "uk_vote_author_answer", columnNames = {"author_id", "answer_id"})})

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "value", column = @Column(name = "value", nullable = false))})
    private VoteValue value;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = true)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = true)
    private Answer answer;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PreUpdate
    @PrePersist
    private void beforeAnySave() {
        validateParent();
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        updatedAt = Instant.now();
    }

    private void validateParent() {
        if ((question == null) == (answer == null)) {
            throw new InvalidAssociationException("Vote must relate to exactly one of Question or Answer");
        }
    }
}
