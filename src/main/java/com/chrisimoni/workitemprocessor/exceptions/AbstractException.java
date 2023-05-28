package com.chrisimoni.workitemprocessor.exceptions;

import org.springframework.http.HttpStatus;

public class AbstractException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    HttpStatus status;

    public AbstractException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus(){
        return this.status;
    }
    public void setStatus(HttpStatus status){
        this.status = status;
    }
}
