package com.freakflow.backend.domain.valueobject;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class TagName {
    private String value;

    protected TagName() {}

    public TagName(String name){
        String norm=name.toLowerCase().trim();
        if (!norm.matches("^[a-z0-9-]+$")) {
            throw new IllegalArgumentException("Invalid tag name: " + name);
        }
        this.value = norm;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagName)) return false;
        return value.equals(((TagName)o).value);
    }
    @Override
    public int hashCode() { return Objects.hash(value); }
    @Override
    public String toString() { return value; }

}
