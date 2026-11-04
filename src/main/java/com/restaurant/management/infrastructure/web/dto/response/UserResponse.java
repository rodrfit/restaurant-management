package com.restaurant.management.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.management.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados de resposta do usuário")
public record UserResponse(
        @JsonProperty("id_usuario")
        Long id,
        String name,
        String email,
        String login,
        @JsonProperty("tipo_usuario")
        @JsonAlias({"userType"})
        String tipoUsuario,
        @JsonProperty("id_tipo_usuario")
        Long idTipoUsuario,
        @JsonProperty("descricao_tipo_usuario")
        String descricaoTipoUsuario,
        LocalDateTime lastChange,
        AddressResponse address
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getUserType() != null ? user.getUserType().getName() :  user.getClass().getSimpleName(),
                user.getUserType() != null ? user.getUserType().getId() : null,
                user.getUserType() != null ? user.getUserType().getObservation() : null,
                user.getLastChange(),
                AddressResponse.from(user.getAddress())
        );
    }
}
