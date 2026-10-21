package com.restaurant.management.domain.repository;

import com.restaurant.management.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {

    Optional<Restaurant> findById(Long id);

    List<Restaurant> findAll();

    Restaurant save(Restaurant restaurant);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    Optional<Restaurant> findByNameIgnoreCase(String name);
}
