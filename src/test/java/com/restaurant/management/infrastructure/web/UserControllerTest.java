package com.restaurant.management.infrastructure.web;

import com.restaurant.management.application.command.ChangePasswordCommand;
import com.restaurant.management.application.command.CreateUserCommand;
import com.restaurant.management.application.command.UpdateUserCommand;
import com.restaurant.management.application.service.UserService;
import com.restaurant.management.domain.model.Address;
import com.restaurant.management.domain.model.User;
import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.infrastructure.web.dto.request.AddressRequest;
import com.restaurant.management.infrastructure.web.dto.request.CreateUserRequest;
import com.restaurant.management.infrastructure.web.dto.request.UpdatePasswordRequest;
import com.restaurant.management.infrastructure.web.dto.request.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class UserControllerTest {

    private final UserService service = Mockito.mock(UserService.class);
    private final UserControllerV1 controller = new UserControllerV1(service);

    private User sampleUser() {
        UserType type = new UserType();
        type.setId(1L);
        type.setName("Cliente");
        type.setObservation("Cliente do sistema");

        Address address = new Address();
        address.setStreet("Rua A");
        address.setNumber("10");
        address.setCity("São Paulo");
        address.setZipCode("01000-000");

        User user = new User();
        user.setId(1L);
        user.setName("João Silva");
        user.setEmail("joao@email.com");
        user.setLogin("joaosilva");
        user.setUserType(type);
        user.setAddress(address);
        user.setLastChange(LocalDateTime.now());
        return user;
    }

    @Test
    void testCreateUser() {
        CreateUserRequest request = new CreateUserRequest(
                "João Silva", "joao@email.com", "joaosilva", "senha123",
                1L, new AddressRequest("Rua A", "10", "São Paulo", "01000-000")
        );
        Mockito.when(service.create(any(CreateUserCommand.class))).thenReturn(sampleUser());

        var result = controller.create(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("João Silva", result.getBody().name());
        assertEquals("São Paulo", result.getBody().address().city());
    }

    @Test
    void testUpdateUser() {
        UpdateUserRequest request = new UpdateUserRequest(
                "João Silva", "joao@email.com",
                new AddressRequest("Rua B", "20", "Campinas", "13000-000")
        );
        Mockito.when(service.update(eq(1L), any(UpdateUserCommand.class))).thenReturn(sampleUser());

        var result = controller.update(1L, request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1L, result.getBody().id());
    }

    @Test
    void testUpdatePassword() {
        UpdatePasswordRequest request = new UpdatePasswordRequest("senha123", "novaSenha123");

        var result = controller.updatePassword(1L, request);

        Mockito.verify(service).updatePassword(eq(1L), any(ChangePasswordCommand.class));
        assertEquals(204, result.getStatusCode().value());
    }

    @Test
    void testAssignType() {
        Mockito.when(service.assignType(1L, 2L)).thenReturn(sampleUser());

        var result = controller.assignType(1L, 2L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1L, result.getBody().id());
    }

    @Test
    void testDeleteUser() {
        var result = controller.delete(1L);

        Mockito.verify(service).delete(1L);
        assertEquals(204, result.getStatusCode().value());
    }

    @Test
    void testFindAllUsers() {
        Mockito.when(service.findAll("João")).thenReturn(List.of(sampleUser()));

        var result = controller.findAll("João");

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testFindById() {
        Mockito.when(service.findById(1L)).thenReturn(sampleUser());

        var result = controller.findById(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("João Silva", result.getBody().name());
    }
}
