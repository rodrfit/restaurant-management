package com.restaurant.management.infrastructure.persistence;

import com.restaurant.management.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "userType")
    Optional<UserEntity> findByLogin(String login);

    @EntityGraph(attributePaths = "userType")
    List<UserEntity> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = "userType")
    @Override
    Optional<UserEntity> findById(Long id);

    @EntityGraph(attributePaths = "userType")
    @Override
    List<UserEntity> findAll();

    boolean existsByNameIgnoreCase(String name);
}
