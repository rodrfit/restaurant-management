package com.restaurant.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restaurant.management.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    boolean existsByOwnerId(Long ownerId);
}
