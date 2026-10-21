package com.restaurant.management.infrastructure.web.dto.request;

import com.restaurant.management.application.command.AddressData;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do endereço")
public record AddressRequest(

        @Schema(description = "Logradouro", example = "Rua das Flores")
        String street,

        @Schema(description = "Número", example = "123")
        String number,

        @Schema(description = "Cidade", example = "São Paulo")
        String city,

        @Schema(description = "CEP", example = "01310-100")
        String zipCode
) {

    public AddressData toData() {
        return new AddressData(street, number, city, zipCode);
    }
}
