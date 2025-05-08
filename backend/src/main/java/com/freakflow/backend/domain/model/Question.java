package com.freakflow.backend.domain.model;

import com.freakflow.backend.domain.valueobject.Slug;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;
import java.util.*;

/**
 * Aгрегатный корень — Question вместе со всеми зависимыми коллекциями
 */
@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    /** Заголовок вопроса */
    @Embedded
    private Slug slug;

    /** Полный текст вопроса */

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    /** Статус: открыт или уже есть принятый ответ */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.OPEN;

    /** Автор вопроса */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "question_tag",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Vote> votes = new ArrayList<>();


    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;


    public enum Status {
        OPEN,
        ANSWERED
    }

    public void setTitle(String title) {
        this.title = title;
        this.slug  = Slug.from(title);
    }

    public void acceptAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            throw new IllegalArgumentException("Answer does not belong to this question");
        }
        boolean already = answers.stream().anyMatch(Answer::isAccepted);
        if (already) {
            throw new IllegalStateException("Question already has an accepted answer");
        }
        answer.accept();          // устанавливаем accepted = true
        this.status = Status.ANSWERED;
    }

    public void unacceptAnswer(Answer answer) {
        if (!answers.contains(answer) || !answer.isAccepted()) {
            throw new IllegalArgumentException("No such accepted answer to unaccept");
        }
        answer.unaccept();
        this.status = Status.OPEN;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }


    @PrePersist
    protected void onCreate() {
        if (this.slug == null) {
            this.slug = Slug.from(this.title);
        }
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }
    @PreUpdate
    protected void onUpdate() {
        this.slug = Slug.from(this.title);
        this.updatedAt = Instant.now();
    }
}
