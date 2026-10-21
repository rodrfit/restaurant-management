package com.restaurant.management.service.impl;

import com.restaurant.management.dto.request.CreateUserTypeRequest;
import com.restaurant.management.dto.request.UpdateUserTypeRequest;
import com.restaurant.management.dto.response.UserTypeResponse;
import com.restaurant.management.model.UserType;
import com.restaurant.management.repository.UserTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserTypeServiceImpl")
class UserTypeServiceImplTest {

    @Mock
    private UserTypeRepository repository;

    @InjectMocks
    private UserTypeServiceImpl service;

    @Test
    @DisplayName("deve criar um tipo de usuário com observação")
    void shouldCreateUserTypeWithObservation() {
        CreateUserTypeRequest request = new CreateUserTypeRequest("Cliente", "Usuário que realiza pedidos no sistema");

        when(repository.existsByNameIgnoreCase("Cliente")).thenReturn(false);
        when(repository.save(any(UserType.class))).thenAnswer(invocation -> {
            UserType entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        UserTypeResponse response = service.create(request);

        assertEquals(1L, response.id());
        assertEquals("Cliente", response.name());
        assertEquals("Usuário que realiza pedidos no sistema", response.observation());
    }

    @Test
    @DisplayName("deve atualizar a observação de um tipo de usuário")
    void shouldUpdateUserTypeObservation() {
        UserType existing = new UserType();
        existing.setId(1L);
        existing.setName("Cliente");
        existing.setObservation("Antiga observação");

        UpdateUserTypeRequest request = new UpdateUserTypeRequest("Cliente", "Nova observação do cliente");

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(repository.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserTypeResponse response = service.update(1L, request);

        assertEquals("Cliente", response.name());
        assertEquals("Nova observação do cliente", response.observation());
    }
}
