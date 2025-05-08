package com.freakflow.backend.domain.valueobject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class VoteValue {
    private int value;

    protected VoteValue() {}

    public VoteValue(int value) {
        if (value != 1 && value != -1) {
            throw new IllegalArgumentException("Vote must be +1 or -1");
        }
        this.value = value;
    }

    public int getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoteValue)) return false;
        return value == ((VoteValue)o).value;
    }
    @Override
    public int hashCode() { return Objects.hash(value); }
    @Override
    public String toString() { return String.valueOf(value); }
}
