package com.restaurant.management.infrastructure.web.dto.response;

import com.restaurant.management.domain.model.Restaurant;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de resposta do restaurante")
public record RestaurantResponse(

        @Schema(description = "ID do restaurante", example = "1")
        Long id,

        @Schema(description = "Nome do restaurante", example = "Sabor do Bairro")
        String nome,

        @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123")
        String endereco,

        @Schema(description = "Tipo de cozinha", example = "Italiana")
        String tipoCozinha,

        @Schema(description = "Horário de funcionamento", example = "Segunda a Domingo, 18:00 - 23:00")
        String horarioFuncionamento,

        @Schema(description = "ID do proprietário do restaurante", example = "1")
        Long ownerId
) {
    public static RestaurantResponse from(Restaurant restaurant) {
        if (restaurant == null) return null;

        Long ownerId = restaurant.getOwner() != null ? restaurant.getOwner().getId() : null;

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getNome(),
                restaurant.getEndereco(),
                restaurant.getTipoCozinha(),
                restaurant.getHorarioFuncionamento(),
                ownerId
        );
    }
}