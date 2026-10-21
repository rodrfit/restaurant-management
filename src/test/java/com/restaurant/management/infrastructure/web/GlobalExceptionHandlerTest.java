package com.restaurant.management.infrastructure.web;

import com.restaurant.management.domain.exception.EmailAlreadyExistsException;
import com.restaurant.management.domain.exception.ExceptionMessages;
import com.restaurant.management.domain.exception.InvalidPasswordException;
import com.restaurant.management.domain.exception.MenuItemNotFoundException;
import com.restaurant.management.domain.exception.RestaurantNotFoundException;
import com.restaurant.management.domain.exception.UserNotFoundException;
import com.restaurant.management.domain.exception.UserTypeAlreadyExistsException;
import com.restaurant.management.domain.exception.UserTypeNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Nested
    @DisplayName("quando trata exceções de negócio")
    class BusinessExceptionTests {

        @Test
        @DisplayName("deve retornar 409 para e-mail já cadastrado")
        void shouldReturnConflictForEmailAlreadyExists() {
            ProblemDetail detail = handler.handleEmailAlreadyExists(new EmailAlreadyExistsException("joao@email.com"));

            assertEquals(HttpStatus.CONFLICT.value(), detail.getStatus());
            assertNotNull(detail.getDetail());
            assertTrue(detail.getDetail().contains("joao@email.com"));
        }

        @Test
        @DisplayName("deve retornar 409 para tipo de usuário já cadastrado")
        void shouldReturnConflictForUserTypeAlreadyExists() {
            ProblemDetail detail = handler.handleUserTypeAlreadyExists(new UserTypeAlreadyExistsException("Cliente"));

            assertEquals(HttpStatus.CONFLICT.value(), detail.getStatus());
        }

        @Test
        @DisplayName("deve retornar 404 para usuário não encontrado")
        void shouldReturnNotFoundForMissingUser() {
            ProblemDetail detail = handler.handleUserNotFound(new UserNotFoundException(99L));

            assertEquals(HttpStatus.NOT_FOUND.value(), detail.getStatus());
            assertNotNull(detail.getDetail());
            assertTrue(detail.getDetail().contains("99"));
        }

        @Test
        @DisplayName("deve retornar 404 para tipo de usuário não encontrado")
        void shouldReturnNotFoundForMissingUserType() {
            ProblemDetail detail = handler.handleUserTypeNotFound(new UserTypeNotFoundException(7L));

            assertEquals(HttpStatus.NOT_FOUND.value(), detail.getStatus());
        }

        @Test
        @DisplayName("deve retornar 404 para restaurante não encontrado")
        void shouldReturnNotFoundForMissingRestaurant() {
            ProblemDetail detail = handler.handleRestaurantNotFound(new RestaurantNotFoundException(5L));

            assertEquals(HttpStatus.NOT_FOUND.value(), detail.getStatus());
            assertTrue(detail.getDetail().contains("5"));
        }

        @Test
        @DisplayName("deve retornar 404 para item do menu não encontrado")
        void shouldReturnNotFoundForMissingMenuItem() {
            ProblemDetail detail = handler.handleMenuItemNotFound(new MenuItemNotFoundException(8L));

            assertEquals(HttpStatus.NOT_FOUND.value(), detail.getStatus());
            assertTrue(detail.getDetail().contains("8"));
        }

        @Test
        @DisplayName("deve retornar 400 para senha inválida")
        void shouldReturnBadRequestForInvalidPassword() {
            ProblemDetail detail = handler.handleInvalidPassword(
                    new InvalidPasswordException(ExceptionMessages.INVALID_CURRENT_PASSWORD));

            assertEquals(HttpStatus.BAD_REQUEST.value(), detail.getStatus());
            assertEquals(ExceptionMessages.INVALID_CURRENT_PASSWORD, detail.getDetail());
        }
    }

    @Nested
    @DisplayName("quando trata erros de validação e infraestrutura")
    class ValidationAndInfrastructureTests {

        @Test
        @DisplayName("deve retornar 400 com mensagens dos campos inválidos")
        void shouldReturnBadRequestForValidationErrors() {
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(
                    new FieldError("createUserRequest", "email", ExceptionMessages.EMAIL_INVALID),
                    new FieldError("createUserRequest", "name", ExceptionMessages.NAME_REQUIRED)
            ));

            ProblemDetail detail = handler.handleValidation(ex);

            assertEquals(HttpStatus.BAD_REQUEST.value(), detail.getStatus());
            assertNotNull(detail.getDetail());
            assertTrue(detail.getDetail().contains(ExceptionMessages.EMAIL_INVALID));
            assertTrue(detail.getDetail().contains(ExceptionMessages.NAME_REQUIRED));
        }

        @Test
        @DisplayName("deve retornar 409 com mensagem genérica para violação de integridade no banco")
        void shouldReturnConflictForDataIntegrityViolation() {
            ProblemDetail detail = handler.handleDataIntegrityViolation(
                    new DataIntegrityViolationException("duplicate"));

            assertEquals(HttpStatus.CONFLICT.value(), detail.getStatus());
            assertEquals("Operação viola uma restrição de integridade (registro duplicado ou em uso)",
                    detail.getDetail());
        }

        @Test
        @DisplayName("deve retornar 500 para erro inesperado")
        void shouldReturnInternalServerErrorForUnexpectedException() {
            ProblemDetail detail = handler.handleUnexpected(new RuntimeException("boom"));

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), detail.getStatus());
            assertEquals("Erro interno inesperado", detail.getDetail());
        }
    }
}
