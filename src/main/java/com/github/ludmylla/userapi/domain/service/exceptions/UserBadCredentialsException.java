package com.github.ludmylla.userapi.domain.service.exceptions;

public class UserBadCredentialsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserBadCredentialsException(String message) {
            super(message);
    }

     public UserBadCredentialsException(String message, Throwable cause) {
            super(message, cause);
    }

}
