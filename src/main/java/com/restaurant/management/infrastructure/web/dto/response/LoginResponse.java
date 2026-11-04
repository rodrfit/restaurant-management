package com.restaurant.management.infrastructure.web.dto.response;

import com.restaurant.management.domain.exception.ExceptionMessages;
import com.restaurant.management.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "Resposta da validação de login")
public record LoginResponse(
        boolean valid,
        String message,
        Long userId,
        String type
) {

    public static LoginResponse success(User user) {
        return new LoginResponse(
                true,
                ExceptionMessages.LOGIN_SUCCESS,
                user.getId(),
                Objects.nonNull(user.getUserType()) ? user.getUserType().getName() : null
        );
    }

    public static LoginResponse failure() {
        return new LoginResponse(false, ExceptionMessages.INVALID_LOGIN_CREDENTIALS, null, null);
    }
}
