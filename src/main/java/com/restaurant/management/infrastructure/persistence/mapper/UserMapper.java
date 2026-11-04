package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.model.User;
import com.restaurant.management.infrastructure.persistence.entity.UserEntity;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public final class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setLogin(entity.getLogin());
        user.setPassword(entity.getPassword());
        user.setLastChange(entity.getLastChange());
        user.setAddress(AddressMapper.toDomain(entity.getAddress()));
        user.setUserType(UserTypeMapper.toDomain(entity.getUserType()));
        return user;
    }

    public static UserEntity toEntity(User domain) {
        if (Objects.isNull(domain)) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setEmail(domain.getEmail());
        entity.setLogin(domain.getLogin());
        entity.setPassword(domain.getPassword());
        entity.setLastChange(domain.getLastChange());
        entity.setAddress(AddressMapper.toEntity(domain.getAddress()));
        entity.setUserType(UserTypeMapper.toEntity(domain.getUserType()));
        return entity;
    }
}
