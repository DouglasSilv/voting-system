package com.sicredi.votingsystem.exception;


import org.springframework.http.HttpStatus;

public class ApiException extends Exception {

    private static final long serialVersionUID = -464262467246746724L;

    final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
