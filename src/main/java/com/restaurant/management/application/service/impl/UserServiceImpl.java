package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.AddressData;
import com.restaurant.management.application.command.ChangePasswordCommand;
import com.restaurant.management.application.command.CreateUserCommand;
import com.restaurant.management.application.command.UpdateUserCommand;
import com.restaurant.management.application.service.UserService;
import com.restaurant.management.domain.exception.EmailAlreadyExistsException;
import com.restaurant.management.domain.exception.ExceptionMessages;
import com.restaurant.management.domain.exception.InvalidPasswordException;
import com.restaurant.management.domain.exception.UserNotFoundException;
import com.restaurant.management.domain.exception.UserTypeNotFoundException;
import com.restaurant.management.domain.model.Address;
import com.restaurant.management.domain.model.User;
import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.domain.port.PasswordHasher;
import com.restaurant.management.domain.repository.UserRepository;
import com.restaurant.management.domain.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserTypeRepository userTypeRepository;
    private final PasswordHasher passwordHasher;

    @Override
    @Transactional
    public User create(CreateUserCommand command) {
        validateEmailUniqueness(command.email());

        UserType userType = userTypeRepository.findById(command.idTipoUsuario())
                .orElseThrow(() -> new UserTypeNotFoundException(command.idTipoUsuario()));

        User user = new User();
        user.setName(command.name());
        user.setEmail(command.email());
        user.setLogin(command.login());
        user.setPassword(passwordHasher.hash(command.password()));
        user.setUserType(userType);
        user.setAddress(toAddress(command.address()));
        user.setLastChange(LocalDateTime.now());
        return repository.save(user);
    }

    @Override
    @Transactional
    public User update(Long id, UpdateUserCommand command) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        validateEmailUpdate(user.getEmail(), command.email());

        user.setName(command.name());
        user.setEmail(command.email());
        if (command.address() != null) {
            user.setAddress(toAddress(command.address()));
        }
        user.setLastChange(LocalDateTime.now());
        return repository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(Long id, ChangePasswordCommand command) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!passwordHasher.matches(command.actualPassword(), user.getPassword())) {
            throw new InvalidPasswordException(ExceptionMessages.INVALID_CURRENT_PASSWORD);
        }

        if (passwordHasher.matches(command.newPassword(), user.getPassword())) {
            throw new InvalidPasswordException(ExceptionMessages.NEW_PASSWORD_SAME_AS_CURRENT);
        }

        user.setPassword(passwordHasher.hash(command.newPassword()));
        user.setLastChange(LocalDateTime.now());
        repository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        repository.delete(user);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional
    public User assignType(Long userId, Long userTypeId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        UserType userType = userTypeRepository.findById(userTypeId)
                .orElseThrow(() -> new UserTypeNotFoundException(userTypeId));

        user.setUserType(userType);
        user.setLastChange(LocalDateTime.now());
        return repository.save(user);
    }

    @Override
    public List<User> findAll(String name) {
        if (name != null && !name.isBlank()) {
            return repository.findByNameContainingIgnoreCase(name);
        }
        return repository.findAll();
    }

    private void validateEmailUpdate(String currentEmail, String newEmail) {
        if (!currentEmail.equalsIgnoreCase(newEmail)) {
            validateEmailUniqueness(newEmail);
        }
    }

    private void validateEmailUniqueness(String email) {
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    private Address toAddress(AddressData data) {
        if (data == null) {
            return null;
        }
        return new Address(data.street(), data.number(), data.city(), data.zipCode());
    }
}
