package com.chrisimoni.workitemprocessor.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AbstractException {
    public NotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
