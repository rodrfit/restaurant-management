package com.restaurant.management.infrastructure.persistence;

import com.restaurant.management.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    List<RestaurantEntity> findByNomeContainingIgnoreCase(String name);
}
