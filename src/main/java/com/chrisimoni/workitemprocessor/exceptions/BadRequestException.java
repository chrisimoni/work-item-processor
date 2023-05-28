package com.chrisimoni.workitemprocessor.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractException {
    private static final long serialVersionUID = 1L;

    public BadRequestException(HttpStatus status, String message) {
        super(status, message);
    }
}
