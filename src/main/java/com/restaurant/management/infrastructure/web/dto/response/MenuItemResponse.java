package com.restaurant.management.infrastructure.web.dto.response;

import com.restaurant.management.domain.model.MenuItem;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de resposta do item do menu")
public record MenuItemResponse(

        @Schema(description = "ID do item do menu", example = "1")
        Long id,

        @Schema(description = "Nome do item do menu", example = "Pizza Margherita")
        String nome,

        @Schema(description = "Descrição do item do menu", example = "Pizza artesanal com molho de tomate e mussarela")
        String descricao,

        @Schema(description = "Preço do item do menu", example = "39.90")
        Double preco,

        @Schema(description = "Disponibilidade no local", example = "true")
        Boolean disponivelNoLocal,

        @Schema(description = "Caminho da foto do item", example = "/images/pizza-margherita.jpg")
        String fotoPath,

        @Schema(description = "ID do restaurante responsável pelo item", example = "1")
        Long restaurantId
) {
    public static MenuItemResponse from(MenuItem menuItem) {
        if (menuItem == null) return null;

        Long restaurantId = menuItem.getRestaurant() != null ? menuItem.getRestaurant().getId() : null;

        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getNome(),
                menuItem.getDescricao(),
                menuItem.getPreco(),
                menuItem.getDisponivelNoLocal(),
                menuItem.getFotoPath(),
                restaurantId
        );
    }
}