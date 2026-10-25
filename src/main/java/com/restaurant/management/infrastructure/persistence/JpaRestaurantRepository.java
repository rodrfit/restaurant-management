package com.restaurant.management.infrastructure.persistence;

import com.restaurant.management.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    @EntityGraph(attributePaths = {"owner", "owner.userType"})
    List<RestaurantEntity> findByNomeContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = {"owner", "owner.userType"})
    @Override
    Optional<RestaurantEntity> findById(Long id);

    @EntityGraph(attributePaths = {"owner", "owner.userType"})
    @Override
    List<RestaurantEntity> findAll();
}