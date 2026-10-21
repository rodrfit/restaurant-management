package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.CreateUserTypeCommand;
import com.restaurant.management.application.command.UpdateUserTypeCommand;
import com.restaurant.management.domain.exception.UserTypeAlreadyExistsException;
import com.restaurant.management.domain.exception.UserTypeNotFoundException;
import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.domain.repository.UserTypeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
        CreateUserTypeCommand command = new CreateUserTypeCommand("Cliente", "Usuário que realiza pedidos no sistema");

        when(repository.existsByNameIgnoreCase("Cliente")).thenReturn(false);
        when(repository.save(any(UserType.class))).thenAnswer(invocation -> {
            UserType userType = invocation.getArgument(0);
            userType.setId(1L);
            return userType;
        });

        UserType created = service.create(command);

        assertEquals(1L, created.getId());
        assertEquals("Cliente", created.getName());
        assertEquals("Usuário que realiza pedidos no sistema", created.getObservation());
    }

    @Test
    @DisplayName("deve lançar UserTypeAlreadyExistsException ao criar nome já existente")
    void shouldThrowWhenCreatingDuplicatedName() {
        when(repository.existsByNameIgnoreCase("Cliente")).thenReturn(true);

        assertThrows(UserTypeAlreadyExistsException.class,
                () -> service.create(new CreateUserTypeCommand(" Cliente ", null)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deve atualizar a observação de um tipo de usuário mantendo o nome")
    void shouldUpdateUserTypeObservation() {
        UserType existing = new UserType();
        existing.setId(1L);
        existing.setName("Cliente");
        existing.setObservation("Antiga observação");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(UserType.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserType updated = service.update(1L, new UpdateUserTypeCommand("Cliente", "Nova observação do cliente"));

        assertEquals("Cliente", updated.getName());
        assertEquals("Nova observação do cliente", updated.getObservation());
    }

    @Test
    @DisplayName("deve lançar UserTypeAlreadyExistsException ao renomear para nome já existente")
    void shouldThrowWhenRenamingToExistingName() {
        UserType existing = new UserType();
        existing.setId(1L);
        existing.setName("Cliente");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByNameIgnoreCase("Dono")).thenReturn(true);

        assertThrows(UserTypeAlreadyExistsException.class,
                () -> service.update(1L, new UpdateUserTypeCommand("Dono", null)));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deve lançar UserTypeNotFoundException ao excluir id inexistente")
    void shouldThrowWhenDeletingNonexistentType() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(UserTypeNotFoundException.class, () -> service.delete(99L));
        verify(repository, never()).deleteById(any());
    }
}
