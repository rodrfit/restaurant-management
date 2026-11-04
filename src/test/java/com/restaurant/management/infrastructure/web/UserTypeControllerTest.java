package com.restaurant.management.infrastructure.web;

import com.restaurant.management.application.command.CreateUserTypeCommand;
import com.restaurant.management.application.command.UpdateUserTypeCommand;
import com.restaurant.management.application.service.UserTypeService;
import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.infrastructure.web.dto.request.CreateUserTypeRequest;
import com.restaurant.management.infrastructure.web.dto.request.UpdateUserTypeRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class UserTypeControllerTest {

    private final UserTypeService service = Mockito.mock(UserTypeService.class);
    private final UserTypeControllerV1 controller = new UserTypeControllerV1(service);

    private UserType sampleUserType() {
        UserType ut = new UserType();
        ut.setId(1L);
        ut.setName("Cliente");
        ut.setObservation("Observação");
        return ut;
    }

    @Test
    void testCreateUserType() {
        CreateUserTypeRequest request = new CreateUserTypeRequest("Cliente", "Observação");
        Mockito.when(service.create(any(CreateUserTypeCommand.class))).thenReturn(sampleUserType());

        var result = controller.create(request);

        assertEquals(201, result.getStatusCode().value());
        assertEquals("Cliente", result.getBody().name());
    }

    @Test
    void testUpdateUserType() {
        UpdateUserTypeRequest request = new UpdateUserTypeRequest("Cliente", "Observação");
        Mockito.when(service.update(eq(1L), any(UpdateUserTypeCommand.class))).thenReturn(sampleUserType());

        var result = controller.update(1L, request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("Cliente", result.getBody().name());
    }

    @Test
    void testDeleteUserType() {
        var result = controller.delete(1L);

        Mockito.verify(service).delete(1L);
        assertEquals(204, result.getStatusCode().value());
    }

    @Test
    void testFindAllUserTypes() {
        Mockito.when(service.findAll()).thenReturn(List.of(sampleUserType()));

        var result = controller.findAll();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, result.getBody().size());
    }

    @Test
    void testFindById() {
        Mockito.when(service.findById(1L)).thenReturn(sampleUserType());

        var result = controller.findById(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1L, result.getBody().id());
    }
}
