package com.restaurant.management.application.command;

public record UpdateUserCommand(
        String name,
        String email,
        AddressData address
) {
}
