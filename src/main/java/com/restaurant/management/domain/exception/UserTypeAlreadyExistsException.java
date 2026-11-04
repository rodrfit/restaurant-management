package com.restaurant.management.domain.exception;

public class UserTypeAlreadyExistsException extends RuntimeException {
    public UserTypeAlreadyExistsException(String name) {
        super("Tipo de usuário já cadastrado: " + name);
    }
}
