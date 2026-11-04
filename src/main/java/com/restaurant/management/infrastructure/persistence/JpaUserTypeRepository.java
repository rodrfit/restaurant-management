package com.restaurant.management.infrastructure.persistence;

import com.restaurant.management.infrastructure.persistence.entity.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserTypeRepository extends JpaRepository<UserTypeEntity, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<UserTypeEntity> findByNameContainingIgnoreCase(String name);

    Optional<UserTypeEntity> findByNameIgnoreCase(String name);
}
