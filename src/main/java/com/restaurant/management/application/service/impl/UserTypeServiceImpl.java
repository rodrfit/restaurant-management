package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.CreateUserTypeCommand;
import com.restaurant.management.application.command.UpdateUserTypeCommand;
import com.restaurant.management.application.service.UserTypeService;
import com.restaurant.management.domain.exception.UserTypeAlreadyExistsException;
import com.restaurant.management.domain.exception.UserTypeNotFoundException;
import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepository repository;

    @Override
    @Transactional
    public UserType create(CreateUserTypeCommand command) {
        String normalizedName = command.name().trim();
        if (repository.existsByNameIgnoreCase(normalizedName)) {
            throw new UserTypeAlreadyExistsException(normalizedName);
        }

        UserType userType = new UserType();
        userType.setName(normalizedName);
        userType.setObservation(Objects.nonNull(command.observation()) ? command.observation().trim() : null);
        return repository.save(userType);
    }

    @Override
    @Transactional
    public UserType update(Long id, UpdateUserTypeCommand command) {
        UserType userType = repository.findById(id)
                .orElseThrow(() -> new UserTypeNotFoundException(id));

        String normalizedName = command.name().trim();
        if (!userType.getName().equalsIgnoreCase(normalizedName)
                && repository.existsByNameIgnoreCase(normalizedName)) {
            throw new UserTypeAlreadyExistsException(normalizedName);
        }

        userType.setName(normalizedName);
        userType.setObservation(Objects.nonNull(command.observation()) ? command.observation().trim() : null);
        return repository.save(userType);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new UserTypeNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Override
    public UserType findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserTypeNotFoundException(id));
    }

    @Override
    public List<UserType> findAll() {
        return repository.findAll();
    }
}
