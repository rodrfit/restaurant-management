package com.restaurant.management.service.impl;

import com.restaurant.management.dto.request.AddressRequest;
import com.restaurant.management.dto.request.CreateUserRequest;
import com.restaurant.management.dto.request.UpdatePasswordRequest;
import com.restaurant.management.dto.request.UpdateUserRequest;
import com.restaurant.management.dto.response.UserResponse;
import com.restaurant.management.enums.UserType;
import com.restaurant.management.exception.EmailAlreadyExistsException;
import com.restaurant.management.exception.ExceptionMessages;
import com.restaurant.management.exception.InvalidPasswordException;
import com.restaurant.management.exception.UserNotFoundException;
import com.restaurant.management.factory.UserFactory;
import com.restaurant.management.model.Address;
import com.restaurant.management.model.Client;
import com.restaurant.management.model.User;
import com.restaurant.management.repository.UserRepository;
import com.restaurant.management.repository.UserTypeRepository;
import com.restaurant.management.service.PasswordService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl")
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserFactory userFactory;

    @Mock
    private PasswordService passwordService;

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private Client existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new Client();
        existingUser.setId(1L);
        existingUser.setName("João Silva");
        existingUser.setEmail("joao@email.com");
        existingUser.setLogin("joaosilva");
        existingUser.setPassword("$2a$hashed");
        existingUser.setLastChange(LocalDateTime.of(2026, 8, 29, 14, 0));

        Address address = new Address();
        address.setStreet("Rua das Flores");
        address.setNumber("100");
        address.setCity("São Paulo");
        address.setZipCode("01001-000");
        existingUser.setAddress(address);
    }

    @Nested
    @DisplayName("quando cria um usuário")
    class CreateTests {

        @Test
        @DisplayName("deve criar usuário com senha hasheada e retornar response")
        void shouldCreateUserWithHashedPassword() {
            CreateUserRequest request = new CreateUserRequest(
                    "João Silva",
                    "joao@email.com",
                    "joaosilva",
                    "senha123",
                    UserType.CLIENT,
                    1L,
                    new AddressRequest("Rua das Flores", "100", "São Paulo", "01001-000")
            );
            Client entity = new Client();
            entity.setName(request.name());
            entity.setEmail(request.email());
            entity.setLogin(request.login());
            com.restaurant.management.model.UserType userType = new com.restaurant.management.model.UserType();
            userType.setId(1L);
            userType.setName("Cliente");

            when(repository.existsByEmail(request.email())).thenReturn(false);
            when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType));
            when(userFactory.createEntity(request, userType)).thenReturn(entity);
            when(passwordService.hash(request.password())).thenReturn("$2a$hashed");
            when(repository.save(any(User.class))).thenAnswer(invocation -> {
                User saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
            });

            UserResponse response = userService.create(request);

            assertEquals(1L, response.id());
            assertEquals("João Silva", response.name());
            assertEquals("joao@email.com", response.email());
            assertEquals("Client", response.tipoUsuario());
            verify(passwordService).hash("senha123");
            verify(repository).save(entity);
            assertEquals("$2a$hashed", entity.getPassword());
        }

        @Test
        @DisplayName("deve lançar EmailAlreadyExistsException quando o e-mail já existir")
        void shouldThrowWhenEmailAlreadyExists() {
            CreateUserRequest request = new CreateUserRequest(
                    "João Silva",
                    "joao@email.com",
                    "joaosilva",
                    "senha123",
                    UserType.CLIENT,
                    null
            );
            when(repository.existsByEmail(request.email())).thenReturn(true);

            EmailAlreadyExistsException ex = assertThrows(
                    EmailAlreadyExistsException.class,
                    () -> userService.create(request)
            );

            assertTrue(ex.getMessage().contains("joao@email.com"));
            verify(repository, never()).save(any());
            verify(userFactory, never()).createEntity(any());
        }
    }

    @Nested
    @DisplayName("quando busca usuários")
    class FindTests {

        @Test
        @DisplayName("deve buscar por id quando o usuário existir")
        void shouldFindByIdWhenUserExists() {
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));

            UserResponse response = userService.findById(1L);

            assertEquals(1L, response.id());
            assertEquals("João Silva", response.name());
            assertEquals("joaosilva", response.login());
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

            List<UserResponse> result = userService.findAll(null);

            assertEquals(1, result.size());
            assertEquals("João Silva", result.getFirst().name());
            verify(repository).findAll();
            verify(repository, never()).findByNameContainingIgnoreCase(anyString());
        }

        @Test
        @DisplayName("deve listar todos quando o nome for em branco")
        void shouldFindAllWhenNameIsBlank() {
            when(repository.findAll()).thenReturn(List.of(existingUser));

            List<UserResponse> result = userService.findAll("   ");

            assertEquals(1, result.size());
            verify(repository).findAll();
        }

        @Test
        @DisplayName("deve buscar por nome parcial ignorando case")
        void shouldFindByNameWhenNameIsProvided() {
            when(repository.findByNameContainingIgnoreCase("joão")).thenReturn(List.of(existingUser));

            List<UserResponse> result = userService.findAll("joão");

            assertEquals(1, result.size());
            assertEquals("João Silva", result.getFirst().name());
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
            UpdateUserRequest request = new UpdateUserRequest(
                    "João Silva Atualizado",
                    "joao.novo@email.com",
                    new AddressRequest("Rua Nova", "200", "Campinas", "13000-000")
            );
            Address newAddress = new Address();
            newAddress.setStreet("Rua Nova");
            newAddress.setNumber("200");
            newAddress.setCity("Campinas");
            newAddress.setZipCode("13000-000");

            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(repository.existsByEmail("joao.novo@email.com")).thenReturn(false);
            when(userFactory.toAddress(request.address())).thenReturn(newAddress);
            when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            UserResponse response = userService.update(1L, request);

            assertEquals("João Silva Atualizado", response.name());
            assertEquals("joao.novo@email.com", response.email());
            assertEquals("Campinas", response.address().city());
            assertNotNull(existingUser.getLastChange());
            verify(repository).save(existingUser);
        }

        @Test
        @DisplayName("não deve validar unicidade quando o e-mail permanecer o mesmo")
        void shouldNotValidateUniquenessWhenEmailUnchanged() {
            UpdateUserRequest request = new UpdateUserRequest(
                    "João Silva Atualizado",
                    "joao@email.com",
                    null
            );
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

            userService.update(1L, request);

            verify(repository, never()).existsByEmail(anyString());
        }

        @Test
        @DisplayName("deve lançar EmailAlreadyExistsException ao trocar para e-mail já cadastrado")
        void shouldThrowWhenUpdatingToExistingEmail() {
            UpdateUserRequest request = new UpdateUserRequest(
                    "João Silva",
                    "outro@email.com",
                    null
            );
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(repository.existsByEmail("outro@email.com")).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> userService.update(1L, request));
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar UserNotFoundException quando o usuário não existir")
        void shouldThrowWhenUpdatingNonexistentUser() {
            UpdateUserRequest request = new UpdateUserRequest("Nome", "email@email.com", null);
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.update(99L, request));
        }
    }

    @Nested
    @DisplayName("quando atualiza a senha")
    class UpdatePasswordTests {

        @Test
        @DisplayName("deve atualizar a senha quando a senha atual for válida e a nova for diferente")
        void shouldUpdatePasswordSuccessfully() {
            UpdatePasswordRequest request = new UpdatePasswordRequest("senha123", "senhaNova456");
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(passwordService.matches("senha123", existingUser.getPassword())).thenReturn(true);
            when(passwordService.matches("senhaNova456", existingUser.getPassword())).thenReturn(false);
            when(passwordService.hash("senhaNova456")).thenReturn("$2a$newHash");

            userService.updatePassword(1L, request);

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(repository).save(captor.capture());
            assertEquals("$2a$newHash", captor.getValue().getPassword());
            assertNotNull(captor.getValue().getLastChange());
        }

        @Test
        @DisplayName("deve lançar InvalidPasswordException quando a senha atual for inválida")
        void shouldThrowWhenCurrentPasswordIsInvalid() {
            UpdatePasswordRequest request = new UpdatePasswordRequest("errada", "senhaNova456");
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(passwordService.matches("errada", existingUser.getPassword())).thenReturn(false);

            InvalidPasswordException ex = assertThrows(
                    InvalidPasswordException.class,
                    () -> userService.updatePassword(1L, request)
            );

            assertEquals(ExceptionMessages.INVALID_CURRENT_PASSWORD, ex.getMessage());
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar InvalidPasswordException quando a nova senha for igual à atual")
        void shouldThrowWhenNewPasswordEqualsCurrent() {
            UpdatePasswordRequest request = new UpdatePasswordRequest("senha123", "senha123");
            when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
            when(passwordService.matches("senha123", existingUser.getPassword())).thenReturn(true);

            InvalidPasswordException ex = assertThrows(
                    InvalidPasswordException.class,
                    () -> userService.updatePassword(1L, request)
            );

            assertEquals(ExceptionMessages.NEW_PASSWORD_SAME_AS_CURRENT, ex.getMessage());
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("deve lançar UserNotFoundException quando o usuário não existir")
        void shouldThrowWhenUserNotFoundOnPasswordUpdate() {
            UpdatePasswordRequest request = new UpdatePasswordRequest("senha123", "senhaNova456");
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.updatePassword(99L, request));
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
