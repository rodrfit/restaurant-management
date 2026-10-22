package com.restaurant.management.infrastructure.persistence;

import com.restaurant.management.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByLogin(String login);

    List<UserEntity> findByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
