package com.restaurant.management.application.service.impl;

import com.restaurant.management.application.command.AddressData;
import com.restaurant.management.application.command.ChangePasswordCommand;
import com.restaurant.management.application.command.CreateUserCommand;
import com.restaurant.management.application.command.UpdateUserCommand;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl")
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserTypeRepository userTypeRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UserServiceImpl userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        UserType userType = new UserType();
        userType.setId(1L);
        userType.setName("Cliente");

        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("João Silva");
        existingUser.setEmail("joao@email.com");
        existingUser.setLogin("joaosilva");
        existingUser.setPassword("$2a$hashed");
        existingUser.setUserType(userType);
        existingUser.setLastChange(LocalDateTime.of(2020, 1, 1, 0, 0));
        existingUser.setAddress(new Address("Rua das Flores", "100", "São Paulo", "01001-000"));
    }

    @Nested
    @DisplayName("quando cria um usuário")
    class CreateTests {

        @Test
        @DisplayName("deve criar usuário com senha hasheada e registrar lastChange")
        void shouldCreateUserWithHashedPassword() {
            UserType userType = new UserType();
            userType.setId(10L);
            userType.setName("Cliente");

            CreateUserCommand command = new CreateUserCommand(
                    "João Silva",
                    "joao@email.com",
                    "joaosilva",
                    "senha123",
                    10L,
                    new AddressData("Rua das Flores", "100", "São Paulo", "01001-000")
            );

            when(repository.existsByEmail(command.email())).thenReturn(false);
            when(userTypeRepository.findById(10L)).thenReturn(Optional.of(userType));
            when(passwordHasher.hash(command.password())).thenReturn("$2a$hashed");
            when(repository.save(any(User.class))).thenAnswer(invocation -> {
                User saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
            });

            User created = userService.create(command);

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(repository).save(captor.capture());
            assertEquals(1L, created.getId());
            assertEquals("João Silva", created.getName());
            assertEquals("joao@email.com", created.getEmail());
            assertEquals("Cliente", created.getUserType().getName());
            assertEquals("São Paulo", created.getAddress().getCity());
            assertEquals("$2a$hashed", captor.getValue().getPassword());
            assertNotNull(captor.getValue().getLastChange());
            verify(passwordHasher).hash("senha123");
        }

        @Test
        @DisplayName("deve lançar EmailAlreadyExistsException quando o e-mail já existir")
        void shouldThrowWhenEmailAlreadyExists() {
            CreateUserCommand command = new CreateUserCommand(
                    "João Silva", "joao@email.com", "joaosilva", "senha123", 1L, null);
            when(repository.existsByEmail(command.email())).thenReturn(true);

            EmailAlreadyExistsException ex = assertThrows(
                    EmailAlreadyExistsException.class,
                    () -> userService.create(command)
            );

            assertTrue(ex.getMessage().contains("joao@email.com"));
            verify(repository, never()).save(any());
            verify(userTypeRepository, never()).findById(anyLong());
        }

        @Test
        @DisplayName("deve lançar UserTypeNotFoundException quando o tipo não existir")
        void shouldThrowWhenUserTypeNotFound() {
            CreateUserCommand command = new CreateUserCommand(
                    "João Silva", "joao@email.com", "joaosilva", "senha123", 99L, null);
            when(repository.existsByEmail(command.email())).thenReturn(false);
            when(userTypeRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(UserTypeNotFoundException.class, () -> userService.create(command));
            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("quando busca usuários")
    class FindTests {

        @Test
        @DisplayName("deve buscar por id quando o usuário existir")
        void shouldFindByIdWhenUserExists() {
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));

            User user = userService.findById(1L);

            assertEquals(1L, user.getId());
            assertEquals("João Silva", user.getName());
            assertEquals("joaosilva", user.getLogin());
        }

        @Test
        @DisplayName("deve lançar UserNotFoundException quando o id não existir")
        void shouldThrowWhenUserNotFoundById() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.findById(99L));
        }

        @Test
        @DisplayName("deve listar todos quando o nome não for informado")
        void shouldFindAllWhenNameIsNull() {
            when(repository.findAll()).thenReturn(List.of(existingUser));

            List<User> result = userService.findAll(null);

            assertEquals(1, result.size());
            assertEquals("João Silva", result.getFirst().getName());
            verify(repository).findAll();
            verify(repository, never()).findByNameContainingIgnoreCase(anyString());
        }

        @Test
        @DisplayName("deve listar todos quando o nome for em branco")
        void shouldFindAllWhenNameIsBlank() {
            when(repository.findAll()).thenReturn(List.of(existingUser));

            List<User> result = userService.findAll("   ");

            assertEquals(1, result.size());
            verify(repository).findAll();
        }

        @Test
        @DisplayName("deve buscar por nome parcial ignorando case")
        void shouldFindByNameWhenNameIsProvided() {
            when(repository.findByNameContainingIgnoreCase("joão")).thenReturn(List.of(existingUser));

            List<User> result = userService.findAll("joão");

            assertEquals(1, result.size());
            assertEquals("João Silva", result.getFirst().getName());
            verify(repository).findByNameContainingIgnoreCase("joão");
            verify(repository, never()).findAll();
        }
    }

    @Nested
    @DisplayName("quando atualiza dados cadastrais")
    class UpdateTests {

        @Test
        @DisplayName("deve atualizar nome, e-mail e endereço e registrar lastChange")
        void shouldUpdateUserData() {
            UpdateUserCommand command = new UpdateUserCommand(
                    "João Silva Atualizado",
                    "joao.novo@email.com",
                    new AddressData("Rua Nova", "200", "Campinas", "13000-000")
            );
            LocalDateTime before = existingUser.getLastChange();

            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(repository.existsByEmail("joao.novo@email.com")).thenReturn(false);
            when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            User updated = userService.update(1L, command);

            assertEquals("João Silva Atualizado", updated.getName());
            assertEquals("joao.novo@email.com", updated.getEmail());
            assertEquals("Campinas", updated.getAddress().getCity());
            assertTrue(updated.getLastChange().isAfter(before));
            verify(repository).save(existingUser);
        }

        @Test
        @DisplayName("não deve validar unicidade quando o e-mail permanecer o mesmo")
        void shouldNotValidateUniquenessWhenEmailUnchanged() {
            UpdateUserCommand command = new UpdateUserCommand("João Silva Atualizado", "joao@email.com", null);
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            userService.update(1L, command);

            verify(repository, never()).existsByEmail(anyString());
        }

        @Test
        @DisplayName("deve lançar EmailAlreadyExistsException ao trocar para e-mail já cadastrado")
        void shouldThrowWhenUpdatingToExistingEmail() {
            UpdateUserCommand command = new UpdateUserCommand("João Silva", "outro@email.com", null);
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(repository.existsByEmail("outro@email.com")).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> userService.update(1L, command));
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar UserNotFoundException quando o usuário não existir")
        void shouldThrowWhenUpdatingNonexistentUser() {
            UpdateUserCommand command = new UpdateUserCommand("Nome", "email@email.com", null);
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.update(99L, command));
        }
    }

    @Nested
    @DisplayName("quando associa um tipo de usuário")
    class AssignTypeTests {

        @Test
        @DisplayName("deve associar o tipo e registrar lastChange")
        void shouldAssignType() {
            UserType newType = new UserType();
            newType.setId(2L);
            newType.setName("Dono de Restaurante");
            LocalDateTime before = existingUser.getLastChange();

            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(userTypeRepository.findById(2L)).thenReturn(Optional.of(newType));
            when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            User result = userService.assignType(1L, 2L);

            assertEquals("Dono de Restaurante", result.getUserType().getName());
            assertTrue(result.getLastChange().isAfter(before));
        }

        @Test
        @DisplayName("deve lançar UserTypeNotFoundException quando o tipo não existir")
        void shouldThrowWhenTypeNotFound() {
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(userTypeRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(UserTypeNotFoundException.class, () -> userService.assignType(1L, 99L));
            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("quando atualiza a senha")
    class UpdatePasswordTests {

        @Test
        @DisplayName("deve atualizar a senha quando a senha atual for válida e a nova for diferente")
        void shouldUpdatePasswordSuccessfully() {
            ChangePasswordCommand command = new ChangePasswordCommand("senha123", "senhaNova456");
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(passwordHasher.matches("senha123", existingUser.getPassword())).thenReturn(true);
            when(passwordHasher.matches("senhaNova456", existingUser.getPassword())).thenReturn(false);
            when(passwordHasher.hash("senhaNova456")).thenReturn("$2a$newHash");

            userService.updatePassword(1L, command);

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(repository).save(captor.capture());
            assertEquals("$2a$newHash", captor.getValue().getPassword());
            assertNotNull(captor.getValue().getLastChange());
        }

        @Test
        @DisplayName("deve lançar InvalidPasswordException quando a senha atual for inválida")
        void shouldThrowWhenCurrentPasswordIsInvalid() {
            ChangePasswordCommand command = new ChangePasswordCommand("errada", "senhaNova456");
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(passwordHasher.matches("errada", existingUser.getPassword())).thenReturn(false);

            InvalidPasswordException ex = assertThrows(
                    InvalidPasswordException.class,
                    () -> userService.updatePassword(1L, command)
            );

            assertEquals(ExceptionMessages.INVALID_CURRENT_PASSWORD, ex.getMessage());
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar InvalidPasswordException quando a nova senha for igual à atual")
        void shouldThrowWhenNewPasswordEqualsCurrent() {
            ChangePasswordCommand command = new ChangePasswordCommand("senha123", "senha123");
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(passwordHasher.matches("senha123", existingUser.getPassword())).thenReturn(true);

            InvalidPasswordException ex = assertThrows(
                    InvalidPasswordException.class,
                    () -> userService.updatePassword(1L, command)
            );

            assertEquals(ExceptionMessages.NEW_PASSWORD_SAME_AS_CURRENT, ex.getMessage());
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar UserNotFoundException quando o usuário não existir")
        void shouldThrowWhenUserNotFoundOnPasswordUpdate() {
            ChangePasswordCommand command = new ChangePasswordCommand("senha123", "senhaNova456");
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.updatePassword(99L, command));
        }
    }

    @Nested
    @DisplayName("quando exclui um usuário")
    class DeleteTests {

        @Test
        @DisplayName("deve excluir o usuário quando o id existir")
        void shouldDeleteWhenUserExists() {
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));

            userService.delete(1L);

            verify(repository).delete(existingUser);
        }

        @Test
        @DisplayName("deve lançar UserNotFoundException quando o id não existir")
        void shouldThrowWhenDeletingNonexistentUser() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.delete(99L));
            verify(repository, never()).delete(any());
        }
    }
}
