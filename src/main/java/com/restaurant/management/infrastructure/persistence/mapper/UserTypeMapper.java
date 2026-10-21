package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.infrastructure.persistence.entity.UserTypeEntity;

public final class UserTypeMapper {
    private UserTypeMapper() {}

    public static UserType toDomain(UserTypeEntity entity) {
        if (entity == null) {
            return null;
        }
        UserType value = new UserType();
        value.setId(entity.getId());
        value.setName(entity.getName());
        value.setObservation(entity.getObservation());
        return value;
    }

    public static UserTypeEntity toEntity(UserType domain) {
        if (domain == null) {
            return null;
        }
        UserTypeEntity entity = new UserTypeEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setObservation(domain.getObservation());
        return entity;
    }
}
