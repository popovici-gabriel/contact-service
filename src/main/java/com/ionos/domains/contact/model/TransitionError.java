package com.ionos.domains.contact.model;

public class TransitionError extends RuntimeException {

    public TransitionError(String message) {
        super(message);
    }

    public TransitionError(String message, Throwable cause) {
        super(message, cause);
    }
}
