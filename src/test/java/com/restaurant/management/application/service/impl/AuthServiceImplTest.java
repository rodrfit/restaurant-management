package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.LoginCommand;
import com.restaurant.management.domain.model.User;
import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.domain.port.PasswordHasher;
import com.restaurant.management.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl")
class AuthServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private AuthServiceImpl authService;

    @Nested
    @DisplayName("quando o login é válido")
    class ValidLoginTests {

        @Test
        @DisplayName("deve retornar o usuário com seu tipo")
        void shouldReturnUserWithType() {
            UserType userType = new UserType();
            userType.setId(1L);
            userType.setName("Cliente");

            User user = new User();
            user.setId(1L);
            user.setLogin("joaosilva");
            user.setPassword("$2a$hashed");
            user.setUserType(userType);

            when(repository.findByLogin("joaosilva")).thenReturn(Optional.of(user));
            when(passwordHasher.matches("senha123", "$2a$hashed")).thenReturn(true);

            Optional<User> result = authService.validateLogin(new LoginCommand("joaosilva", "senha123"));

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            assertEquals("Cliente", result.get().getUserType().getName());
        }
    }

    @Nested
    @DisplayName("quando o login é inválido")
    class InvalidLoginTests {

        @Test
        @DisplayName("deve retornar vazio quando o login não existir")
        void shouldReturnEmptyWhenLoginNotFound() {
            when(repository.findByLogin("inexistente")).thenReturn(Optional.empty());

            Optional<User> result = authService.validateLogin(new LoginCommand("inexistente", "senha123"));

            assertTrue(result.isEmpty());
            verify(passwordHasher, never()).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("deve retornar vazio quando a senha estiver incorreta")
        void shouldReturnEmptyWhenPasswordDoesNotMatch() {
            User user = new User();
            user.setId(1L);
            user.setLogin("joaosilva");
            user.setPassword("$2a$hashed");

            when(repository.findByLogin("joaosilva")).thenReturn(Optional.of(user));
            when(passwordHasher.matches("senhaErrada", "$2a$hashed")).thenReturn(false);

            Optional<User> result = authService.validateLogin(new LoginCommand("joaosilva", "senhaErrada"));

            assertTrue(result.isEmpty());
        }
    }
}
