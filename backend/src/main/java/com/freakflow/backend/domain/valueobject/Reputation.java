package com.freakflow.backend.domain.valueobject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Reputation {
    private int value;

    protected Reputation() {}

    public Reputation(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Reputation cannot be negative");
        }
        this.value = value;
    }

    public int getValue() { return value; }

    public Reputation increaseBy(int delta) {
        return new Reputation(this.value + delta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reputation)) return false;
        return value == ((Reputation)o).value;
    }
    @Override
    public int hashCode() { return Objects.hash(value); }
    @Override
    public String toString() { return String.valueOf(value); }
}
