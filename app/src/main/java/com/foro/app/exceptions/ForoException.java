package com.foro.app.exceptions;

public abstract class ForoException extends RuntimeException {
    public ForoException(String message) {
        super(message);
    }
}

