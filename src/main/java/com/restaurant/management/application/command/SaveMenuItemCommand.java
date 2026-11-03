package com.restaurant.management.application.command;

public record SaveMenuItemCommand(
        Long id,
        String nome,
        String descricao,
        Double preco,
        Boolean disponivelNoLocal,
        String fotoPath,
        Long restaurantId
) {
    public SaveMenuItemCommand(
            String nome,
            String descricao,
            Double preco,
            Boolean disponivelNoLocal,
            String fotoPath,
            Long restaurantId
    ) {
        this(null, nome, descricao, preco, disponivelNoLocal, fotoPath, restaurantId);
    }
}
