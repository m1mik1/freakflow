package com.freakflow.backend.domain.valueobject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class EmailAddress {
    private String value;

    protected EmailAddress() {} // для JPA

    public EmailAddress(String email) {
        String normalized = email.toLowerCase().trim();
        if (!normalized.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email address: " + email);
        }
        this.value = normalized;
    }


    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailAddress)) return false;
        return value.equals(((EmailAddress)o).value);
    }
    @Override
    public int hashCode() { return Objects.hash(value); }
    @Override
    public String toString() { return value; }
}
