package com.github.ludmylla.userapi.domain.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataIntegrityViolationException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public DataIntegrityViolationException(String message) {
        super(message);
    }
}
