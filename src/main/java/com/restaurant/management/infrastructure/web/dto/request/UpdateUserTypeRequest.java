package com.restaurant.management.infrastructure.web.dto.request;

import com.restaurant.management.application.command.UpdateUserTypeCommand;
import com.restaurant.management.domain.exception.ExceptionMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização do tipo de usuário")
public record UpdateUserTypeRequest(

        @NotBlank(message = ExceptionMessages.USER_TYPE_NAME_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.USER_TYPE_NAME_MAX_LENGTH)
        @Schema(description = "Nome do tipo de usuário", example = "Cliente Premium")
        String name,

        @Size(max = 1000, message = ExceptionMessages.USER_TYPE_OBSERVATION_MAX_LENGTH)
        @Schema(description = "Observação do tipo de usuário", example = "Usuário com acesso administrativo para gerenciar restaurantes")
        String observation
) {

    public UpdateUserTypeCommand toCommand() {
        return new UpdateUserTypeCommand(name, observation);
    }
}
