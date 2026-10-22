package com.restaurant.management.infrastructure.web;

import com.restaurant.management.infrastructure.web.dto.request.LoginRequest;
import com.restaurant.management.infrastructure.web.dto.response.LoginResponse;
import com.restaurant.management.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Validação de login do usuário")
public class AuthControllerV1 {

    private final AuthService service;

    @PostMapping("/login")
    @Operation(
            summary = "Validar login",
            description = "Verifica se login e senha são válidos consultando os dados no banco"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultado da validação (válido ou inválido)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(service.validateLogin(request.toCommand())
                .map(LoginResponse::success)
                .orElseGet(LoginResponse::failure));
    }
}
