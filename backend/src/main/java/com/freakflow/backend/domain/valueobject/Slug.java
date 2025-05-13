package com.freakflow.backend.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.text.Normalizer;
import java.util.Objects;

@Embeddable
public class Slug {
    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String value;

    protected Slug() {}

    private Slug(String value) {
        this.value = value;
    }


    public static Slug from(String title) {
        Objects.requireNonNull(title, "title must not be null");
        // Нормализуем строку, убираем диакритические знаки
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        // Приводим к нижнему регистру и обрезаем пробелы
        String lower = normalized.toLowerCase().trim();
        // Заменяем все непробельные последовательности, не являющиеся буквами или цифрами, на дефис
        String raw = lower
                .replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]+", "-")
                .replaceAll("(^-+)|(-+$)", "");
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Cannot generate slug from title: '" + title + "'");
        }
        return new Slug(raw);
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Slug)) return false;
        return value.equals(((Slug)o).value);
    }
    @Override
    public int hashCode() { return Objects.hash(value); }
    @Override
    public String toString() { return value; }
}
