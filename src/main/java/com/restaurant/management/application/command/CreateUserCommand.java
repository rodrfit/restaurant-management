package com.restaurant.management.application.command;

public record CreateUserCommand(
        String name,
        String email,
        String login,
        String password,
        Long idTipoUsuario,
        AddressData address
) {
}
