package com.foro.app.exceptions;

public class BadRequestException extends ForoException {
    public BadRequestException(String message) {
        super(message);
    }
}

