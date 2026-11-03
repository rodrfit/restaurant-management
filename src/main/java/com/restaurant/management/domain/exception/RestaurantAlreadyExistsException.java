package com.restaurant.management.exception;

public class RestaurantAlreadyExistsException extends RuntimeException {
    public RestaurantAlreadyExistsException() {
        super("Retaurante já cadastrado");
    }
}
