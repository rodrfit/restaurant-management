package com.restaurant.management.infrastructure.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.management.domain.model.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de resposta do tipo de usuário")
public record UserTypeResponse(

        @JsonProperty("id_tipo_usuario")
        @Schema(description = "ID do tipo de usuário", example = "1")
        Long id,

        @Schema(description = "Nome do tipo de usuário", example = "Cliente")
        String name,

        @Schema(description = "Observação do tipo de usuário", example = "Usuário que realiza pedidos no sistema")
        String observation
) {
    public static UserTypeResponse from(UserType userType) {
        if (userType == null) return null;
        return new UserTypeResponse(userType.getId(), userType.getName(), userType.getObservation());
    }
}
