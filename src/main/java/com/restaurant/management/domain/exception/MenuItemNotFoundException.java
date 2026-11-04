package com.restaurant.management.domain.exception;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(Long id) {
        super("Item de menu não encontrado com id: " + id);
    }
}
