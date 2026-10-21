package com.restaurant.management.domain.repository;

import com.restaurant.management.domain.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

    Optional<MenuItem> findById(Long id);

    List<MenuItem> findAll();

    MenuItem save(MenuItem menuItem);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<MenuItem> findByNameContainingIgnoreCase(String name);

    Optional<MenuItem> findByNameIgnoreCase(String name);
}
