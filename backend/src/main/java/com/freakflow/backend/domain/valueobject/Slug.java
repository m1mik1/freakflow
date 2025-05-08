package com.freakflow.backend.domain.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
        String raw = Objects.requireNonNull(title, "title must not be null")
                .toLowerCase().trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+)|(-+$)", "");
        if (raw.isEmpty()) {
            throw new IllegalArgumentException("Cannot generate slug from empty title");
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
