package com.restaurant.management.infrastructure.web.dto.request;

import com.restaurant.management.application.command.CreateUserTypeCommand;
import com.restaurant.management.domain.exception.ExceptionMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um novo tipo de usuário")
public record CreateUserTypeRequest(

        @NotBlank(message = ExceptionMessages.USER_TYPE_NAME_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.USER_TYPE_NAME_MAX_LENGTH)
        @Schema(description = "Nome do tipo de usuário", example = "Cliente")
        String name,

        @Size(max = 1000, message = ExceptionMessages.USER_TYPE_OBSERVATION_MAX_LENGTH)
        @Schema(description = "Observação do tipo de usuário", example = "Usuário que realiza pedidos no sistema")
        String observation
) {

    public CreateUserTypeCommand toCommand() {
        return new CreateUserTypeCommand(name, observation);
    }
}
