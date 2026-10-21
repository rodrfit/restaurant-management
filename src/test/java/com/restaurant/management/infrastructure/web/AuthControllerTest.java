package com.restaurant.management.infrastructure.web;

import com.restaurant.management.application.command.LoginCommand;
import com.restaurant.management.application.service.AuthService;
import com.restaurant.management.domain.exception.ExceptionMessages;
import com.restaurant.management.domain.model.User;
import com.restaurant.management.domain.model.UserType;
import com.restaurant.management.infrastructure.web.dto.request.LoginRequest;
import com.restaurant.management.infrastructure.web.dto.response.LoginResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private final AuthService service = Mockito.mock(AuthService.class);
    private final AuthControllerV1 controller = new AuthControllerV1(service);

    @Test
    void testLoginSuccess() {
        UserType userType = new UserType();
        userType.setName("Cliente");
        User user = new User();
        user.setId(1L);
        user.setUserType(userType);
        Mockito.when(service.validateLogin(new LoginCommand("joaosilva", "senha123"))).thenReturn(Optional.of(user));

        var response = controller.login(new LoginRequest("joaosilva", "senha123"));

        assertEquals(200, response.getStatusCode().value());
        LoginResponse body = response.getBody();
        assertTrue(body.valid());
        assertEquals(ExceptionMessages.LOGIN_SUCCESS, body.message());
        assertEquals(1L, body.userId());
        assertEquals("Cliente", body.type());
    }

    @Test
    void testLoginFailure() {
        Mockito.when(service.validateLogin(new LoginCommand("joaosilva", "errada"))).thenReturn(Optional.empty());

        var response = controller.login(new LoginRequest("joaosilva", "errada"));

        assertEquals(200, response.getStatusCode().value());
        LoginResponse body = response.getBody();
        assertFalse(body.valid());
        assertEquals(ExceptionMessages.INVALID_LOGIN_CREDENTIALS, body.message());
        assertNull(body.userId());
        assertNull(body.type());
    }
}
