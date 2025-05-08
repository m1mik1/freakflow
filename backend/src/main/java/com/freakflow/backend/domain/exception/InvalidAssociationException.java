package com.freakflow.backend.domain.exception;

public class InvalidAssociationException extends RuntimeException {
    public InvalidAssociationException(String msg) {
        super(msg);
    }
}
