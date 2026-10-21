package com.restaurant.management.application.command;

public record CreateUserTypeCommand(
        String name,
        String observation
) {
}
