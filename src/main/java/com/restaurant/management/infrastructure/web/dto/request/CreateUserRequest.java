package com.restaurant.management.infrastructure.web.dto.request;

import com.restaurant.management.application.command.CreateUserCommand;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.management.domain.exception.ExceptionMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um novo usuário")
public record CreateUserRequest(

        @NotBlank(message = ExceptionMessages.NAME_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.NAME_MAX_LENGTH)
        @Schema(description = "Nome completo", example = "João Silva")
        String name,

        @Email(message = ExceptionMessages.EMAIL_INVALID)
        @NotBlank(message = ExceptionMessages.EMAIL_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.EMAIL_MAX_LENGTH)
        @Schema(description = "E-mail único do usuário", example = "joao.novo@email.com")
        String email,

        @NotBlank(message = ExceptionMessages.LOGIN_REQUIRED)
        @Size(max = 255, message = ExceptionMessages.LOGIN_MAX_LENGTH)
        @Schema(description = "Login de acesso", example = "joaosilva")
        String login,

        @NotBlank(message = ExceptionMessages.PASSWORD_REQUIRED)
        @Size(min = 6, max = 72, message = ExceptionMessages.PASSWORD_LENGTH)
        @Schema(description = "Senha de acesso (mínimo 6, máximo 72 caracteres)", example = "senha123")
        String password,

        @NotNull(message = ExceptionMessages.TYPE_REQUIRED)
        @JsonProperty("id_tipo_usuario")
        @Schema(description = "ID do tipo de usuário associado ao cadastro", example = "1")
        Long idTipoUsuario,

        @Valid
        @Schema(description = "Endereço do usuário")
        AddressRequest address
) {

    public CreateUserCommand toCommand() {
        return new CreateUserCommand(name, email, login, password, idTipoUsuario,
                address != null ? address.toData() : null);
    }
}
