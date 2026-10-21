package com.restaurant.management.infrastructure.web.dto.request;

import com.restaurant.management.application.command.SaveMenuItemCommand;
import com.restaurant.management.domain.exception.ExceptionMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um novo item do menu")
public record CreateMenuItemRequest(

        @NotBlank(message = ExceptionMessages.NAME_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.NAME_MAX_LENGTH)
        @Schema(description = "Nome do item do menu", example = "Pizza Margherita")
        String nome,

        @NotBlank(message = ExceptionMessages.DESCRIPTION_REQUIRED)
        @Size(max = 1000, message = ExceptionMessages.DESCRIPTION_MAX_LENGTH)
        @Schema(description = "Descrição do item do menu", example = "Pizza artesanal com molho de tomate e mussarela")
        String descricao,

        @NotNull(message = ExceptionMessages.PRICE_REQUIRED)
        @DecimalMin(value = "0.01", message = ExceptionMessages.PRICE_GREATER_THAN_ZERO)
        @Schema(description = "Preço do item do menu", example = "39.90")
        Double preco,

        @NotNull(message = ExceptionMessages.AVAILABILITY_REQUIRED)
        @Schema(description = "Disponibilidade no local", example = "true")
        Boolean disponivelNoLocal,

        @Size(max = 255, message = ExceptionMessages.PHOTO_PATH_MAX_LENGTH)
        @Schema(description = "Caminho da foto do item", example = "/images/pizza-margherita.jpg")
        String fotoPath,

        @NotNull(message = ExceptionMessages.RESTAURANT_ID_REQUIRED)
        @Schema(description = "ID do restaurante responsável pelo item", example = "1")
        Long restaurantId
) {

    public SaveMenuItemCommand toCommand() {
        return new SaveMenuItemCommand(nome, descricao, preco, disponivelNoLocal, fotoPath, restaurantId);
    }
}
