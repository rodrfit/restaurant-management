package com.restaurant.management.infrastructure.web.dto.request;

import com.restaurant.management.application.command.SaveRestaurantCommand;
import com.restaurant.management.domain.exception.ExceptionMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um novo restaurante")
public record CreateRestaurantRequest(

        @NotBlank(message = ExceptionMessages.NAME_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.NAME_MAX_LENGTH)
        @Schema(description = "Nome do restaurante", example = "Sabor do Bairro")
        String nome,

        @NotBlank(message = ExceptionMessages.ADDRESS_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.ADDRESS_MAX_LENGTH)
        @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123")
        String endereco,

        @NotBlank(message = ExceptionMessages.CUISINE_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.CUISINE_MAX_LENGTH)
        @Schema(description = "Tipo de cozinha", example = "Italiana")
        String tipoCozinha,

        @NotBlank(message = ExceptionMessages.OPENING_HOURS_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.OPENING_HOURS_MAX_LENGTH)
        @Schema(description = "Horário de funcionamento", example = "Segunda a Domingo, 18:00 - 23:00")
        String horarioFuncionamento,

        @NotNull(message = ExceptionMessages.OWNER_ID_REQUIRED)
        @Schema(description = "ID do proprietário do restaurante", example = "1")
        Long ownerId
) {

    public SaveRestaurantCommand toCommand() {
        return new SaveRestaurantCommand(nome, endereco, tipoCozinha, horarioFuncionamento, ownerId);
    }
}
