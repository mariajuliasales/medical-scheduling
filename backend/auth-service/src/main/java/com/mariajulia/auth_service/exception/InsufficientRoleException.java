package com.mariajulia.auth_service.exception;

public class InsufficientRoleException extends RuntimeException {

    public InsufficientRoleException(String message) {
        super(message);
    }
}
