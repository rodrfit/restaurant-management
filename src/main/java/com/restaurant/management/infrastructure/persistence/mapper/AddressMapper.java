package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.model.Address;
import com.restaurant.management.infrastructure.persistence.entity.AddressEmbeddable;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public final class AddressMapper {

    public static Address toDomain(AddressEmbeddable entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        Address address = new Address();
        address.setStreet(entity.getStreet());
        address.setNumber(entity.getNumber());
        address.setCity(entity.getCity());
        address.setZipCode(entity.getZipCode());
        return address;
    }

    public static AddressEmbeddable toEntity(Address domain) {
        if (Objects.isNull(domain)) {
            return null;
        }
        return new AddressEmbeddable(
                domain.getStreet(),
                domain.getNumber(),
                domain.getCity(),
                domain.getZipCode()
        );
    }
}
