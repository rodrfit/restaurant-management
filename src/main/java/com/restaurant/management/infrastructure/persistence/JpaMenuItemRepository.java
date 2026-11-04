package com.restaurant.management.infrastructure.persistence;

import com.restaurant.management.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaMenuItemRepository extends JpaRepository<MenuItemEntity, Long> {

    @EntityGraph(attributePaths = {"restaurant", "restaurant.owner", "restaurant.owner.userType"})
    List<MenuItemEntity> findByNomeContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"restaurant", "restaurant.owner", "restaurant.owner.userType"})
    @Override
    Optional<MenuItemEntity> findById(Long id);

    @EntityGraph(attributePaths = {"restaurant", "restaurant.owner", "restaurant.owner.userType"})
    @Override
    List<MenuItemEntity> findAll();
}
