package com.github.ludmylla.userapi.security.exceptions;

public class AuthenticationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
