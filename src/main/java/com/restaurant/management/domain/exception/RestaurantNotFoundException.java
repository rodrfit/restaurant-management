package com.restaurant.management.domain.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long id) {
        super("Restaurante não encontrado com id: " + id);
    }
}
