package com.freakflow.backend.infrastructure.repository.specification;

import com.freakflow.backend.domain.model.Question;
import com.freakflow.backend.domain.model.Tag;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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
    public static Specification<Question> hasAllTags(List<String> tagsFilter) {
        return (root, query, cb) -> {
            // join с таблицей тегов
            Join<Question, Tag> tags = root.join("tags", JoinType.INNER);
            // подзапрос: считаем, сколько из нужных тегов есть у вопроса
            Subquery<Long> sq = query.subquery(Long.class);
            Root<Question> sqRoot = sq.from(Question.class);
            Join<Question,Tag> sqTags = sqRoot.join("tags", JoinType.INNER);

            sq.select(cb.count(sqTags))
                    .where(
                            cb.equal(sqRoot.get("id"), root.get("id")),
                            sqTags.get("name").get("value").in(tagsFilter)
                    );

            // сравниваем: число найденных тегов == размер списка
            return cb.equal(sq, (long) tagsFilter.size());
        };
    }
}
