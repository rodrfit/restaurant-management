package com.restaurant.management.infrastructure.web.dto.request;

import com.restaurant.management.application.command.UpdateUserCommand;
import com.restaurant.management.domain.exception.ExceptionMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Schema(description = "Dados para atualização das informações do usuário (exceto senha)")
public record UpdateUserRequest(

        @NotBlank(message = ExceptionMessages.NAME_REQUIRED)
        @Schema(description = "Nome completo", example = "João Silva Atualizado")
        String name,

        @Email(message = ExceptionMessages.EMAIL_INVALID)
        @NotBlank(message = ExceptionMessages.EMAIL_REQUIRED)
        @Schema(description = "E-mail único do usuário", example = "joao.novo@email.com")
        String email,

        @Valid
        @Schema(description = "Endereço do usuário")
        AddressRequest address
) {

    public UpdateUserCommand toCommand() {
        return new UpdateUserCommand(name, email, Objects.nonNull(address) ? address.toData() : null);
    }
}
