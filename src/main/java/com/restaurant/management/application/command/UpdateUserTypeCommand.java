package com.restaurant.management.application.command;

public record UpdateUserTypeCommand(
        String name,
        String observation
) {
}
