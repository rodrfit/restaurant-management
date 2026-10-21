package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.model.Address;
import com.restaurant.management.infrastructure.persistence.entity.AddressEmbeddable;

public final class AddressMapper {
    private AddressMapper() {}

    public static Address toDomain(AddressEmbeddable entity) {
        if (entity == null) {
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
        if (domain == null) {
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
