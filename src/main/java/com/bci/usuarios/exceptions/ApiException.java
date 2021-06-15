package com.bci.usuarios.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiException {

    private final String message;
    private final int code;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;

    public ApiException(String message,
                        int code,
                        HttpStatus httpStatus,
                        LocalDateTime timestamp) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
