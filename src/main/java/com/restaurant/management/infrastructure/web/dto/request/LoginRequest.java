package com.restaurant.management.infrastructure.web.dto.request;

import com.restaurant.management.application.command.LoginCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import com.restaurant.management.domain.exception.ExceptionMessages;

@Schema(description = "Credenciais para validação de login")
public record LoginRequest(

        @NotBlank(message = ExceptionMessages.LOGIN_REQUIRED)
        @Schema(description = "Login do usuário", example = "joaosilva")
        String login,

        @NotBlank(message = ExceptionMessages.PASSWORD_REQUIRED)
        @Schema(description = "Senha do usuário", example = "senha123")
        String password
) {

    public LoginCommand toCommand() {
        return new LoginCommand(login, password);
    }
}
