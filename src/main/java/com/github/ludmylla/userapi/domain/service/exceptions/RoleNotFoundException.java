package com.github.ludmylla.userapi.domain.service.exceptions;

public class RoleNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -1L;

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
