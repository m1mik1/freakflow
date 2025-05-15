package com.freakflow.backend.infrastructure.repository.specification;

import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.model.Tag;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public final class QuestionSpecs  {
    public static Specification<Question> titleOrBodyContains(String text) {
        return (root, query, cb) -> {
            String pattern = "%" + text.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("body")),  pattern)
            );
        };
    }
    public static Specification<Question> hasAnyTag(List<String> tagNames) {
        return (root, query, cb) -> {
            // Чтобы не дублировать Question при JOIN, указываем distinct
            query.distinct(true);
            // JOIN к таблице tags по коллекции tags в Question
            Join<Question, Tag> tags = root.join("tags");
            // Path к строковому полю внутри Embeddable:
            Path<String> nameValue = tags.get("name").get("value");
            // условие "имя тега IN (:tagNames)"
            return nameValue.in(tagNames);
        };
    }
}
