package com.restaurant.management.infrastructure.web.dto.request;

import com.restaurant.management.application.command.ChangePasswordCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.restaurant.management.domain.exception.ExceptionMessages;

@Schema(description = "Dados para alteração de senha")
public record UpdatePasswordRequest(

        @NotBlank(message = ExceptionMessages.CURRENT_PASSWORD_REQUIRED)
        @Schema(description = "Senha atual do usuário", example = "senhaAntiga123")
        String actualPassword,

        @NotBlank(message = ExceptionMessages.NEW_PASSWORD_REQUIRED)
        @Size(min = 6, max = 72, message = ExceptionMessages.NEW_PASSWORD_LENGTH)
        @Schema(description = "Nova senha (mínimo 6, máximo 72 caracteres)", example = "senhaNova456")
        String newPassword
) {

    public ChangePasswordCommand toCommand() {
        return new ChangePasswordCommand(actualPassword, newPassword);
    }
}
