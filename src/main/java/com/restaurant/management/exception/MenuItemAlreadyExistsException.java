package com.restaurant.management.exception;

public class MenuItemAlreadyExistsException extends RuntimeException {
    public MenuItemAlreadyExistsException(Long id) {
        super("Item de menu ja cadastrado");
    }
}
