package com.restaurant.management.application.command;

public record ChangePasswordCommand(
        String actualPassword,
        String newPassword
) {
}
