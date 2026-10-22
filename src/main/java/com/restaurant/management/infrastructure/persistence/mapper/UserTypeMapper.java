package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.infrastructure.persistence.entity.UserTypeEntity;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public final class UserTypeMapper {

    public static UserType toDomain(UserTypeEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        UserType value = new UserType();
        value.setId(entity.getId());
        value.setName(entity.getName());
        value.setObservation(entity.getObservation());
        return value;
    }

    public static UserTypeEntity toEntity(UserType domain) {
        if (Objects.isNull(domain)) {
            return null;
        }
        UserTypeEntity entity = new UserTypeEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setObservation(domain.getObservation());
        return entity;
    }
}
