package com.restaurant.management.application.command;

public record SaveRestaurantCommand(
        String nome,
        String endereco,
        String tipoCozinha,
        String horarioFuncionamento,
        Long ownerId
) {
}
