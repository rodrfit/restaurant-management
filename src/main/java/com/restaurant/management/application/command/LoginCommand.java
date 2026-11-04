package com.restaurant.management.application.command;

public record LoginCommand(
        String login,
        String password
) {
}
