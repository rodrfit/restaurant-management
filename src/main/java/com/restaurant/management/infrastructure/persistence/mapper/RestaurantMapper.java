package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.model.Restaurant;
import com.restaurant.management.infrastructure.persistence.entity.RestaurantEntity;

public final class RestaurantMapper {
    private RestaurantMapper() {}

    public static Restaurant toDomain(RestaurantEntity entity) {
        if (entity == null) {
            return null;
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setId(entity.getId());
        restaurant.setNome(entity.getNome());
        restaurant.setEndereco(entity.getEndereco());
        restaurant.setTipoCozinha(entity.getTipoCozinha());
        restaurant.setHorarioFuncionamento(entity.getHorarioFuncionamento());
        restaurant.setOwner(UserMapper.toDomain(entity.getOwner()));
        return restaurant;
    }

    public static RestaurantEntity toEntity(Restaurant domain) {
        if (domain == null) {
            return null;
        }
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEndereco(domain.getEndereco());
        entity.setTipoCozinha(domain.getTipoCozinha());
        entity.setHorarioFuncionamento(domain.getHorarioFuncionamento());
        entity.setOwner(UserMapper.toEntity(domain.getOwner()));
        return entity;
    }
}
