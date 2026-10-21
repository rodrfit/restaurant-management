package com.restaurant.management.infrastructure.persistence.mapper;

import com.restaurant.management.domain.model.MenuItem;
import com.restaurant.management.infrastructure.persistence.entity.MenuItemEntity;

public final class MenuItemMapper {
    private MenuItemMapper() {}

    public static MenuItem toDomain(MenuItemEntity entity) {
        if (entity == null) {
            return null;
        }
        MenuItem menuItem = new MenuItem();
        menuItem.setId(entity.getId());
        menuItem.setNome(entity.getNome());
        menuItem.setDescricao(entity.getDescricao());
        menuItem.setPreco(entity.getPreco());
        menuItem.setDisponivelNoLocal(entity.getDisponivelNoLocal());
        menuItem.setFotoPath(entity.getFotoPath());
        menuItem.setRestaurant(RestaurantMapper.toDomain(entity.getRestaurant()));
        return menuItem;
    }

    public static MenuItemEntity toEntity(MenuItem domain) {
        if (domain == null) {
            return null;
        }
        MenuItemEntity entity = new MenuItemEntity();
        entity.setId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setDescricao(domain.getDescricao());
        entity.setPreco(domain.getPreco());
        entity.setDisponivelNoLocal(domain.getDisponivelNoLocal());
        entity.setFotoPath(domain.getFotoPath());
        entity.setRestaurant(RestaurantMapper.toEntity(domain.getRestaurant()));
        return entity;
    }
}
