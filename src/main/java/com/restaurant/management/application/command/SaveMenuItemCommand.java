package com.restaurant.management.application.command;

public record SaveMenuItemCommand(
        String nome,
        String descricao,
        Double preco,
        Boolean disponivelNoLocal,
        String fotoPath,
        Long restaurantId
) {
}
