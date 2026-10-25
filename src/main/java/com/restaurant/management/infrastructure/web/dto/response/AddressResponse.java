package com.restaurant.management.infrastructure.web.dto.response;

import com.restaurant.management.domain.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do endereço")
public record AddressResponse(

        @Schema(description = "Logradouro", example = "Rua das Flores")
        String street,

        @Schema(description = "Número", example = "123")
        String number,

        @Schema(description = "Cidade", example = "São Paulo")
        String city,

        @Schema(description = "CEP", example = "01310-100")
        String zipCode
) {
    public static AddressResponse from(Address address) {
        if (address == null) return null;
        return new AddressResponse(address.getStreet(), address.getNumber(), address.getCity(), address.getZipCode());
    }
}
