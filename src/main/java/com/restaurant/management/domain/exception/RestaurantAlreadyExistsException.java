package com.restaurant.management.domain.exception;

public class RestaurantAlreadyExistsException extends RuntimeException {
    public RestaurantAlreadyExistsException() {
        super("Retaurante já cadastrado");
    }
}
