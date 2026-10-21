package com.restaurant.management.infrastructure.web;

import com.restaurant.management.infrastructure.web.dto.request.CreateUserRequest;
import com.restaurant.management.infrastructure.web.dto.request.UpdatePasswordRequest;
import com.restaurant.management.infrastructure.web.dto.request.UpdateUserRequest;
import com.restaurant.management.infrastructure.web.dto.response.UserResponse;
import com.restaurant.management.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gestão de usuários do sistema")
public class UserControllerV1 {

    private final UserService service;

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário (CLIENT ou RESTAURANT_OWNER) no sistema")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(service.create(request.toCommand())));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente pelo seu ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "E-mail já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<UserResponse> update(
            @Parameter(name = "id", description = "ID do usuário a ser atualizado", example = "1")
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(UserResponse.from(service.update(id, request.toCommand())));
    }

    @PutMapping("/{id}/password")
    @Operation(
            summary = "Alterar senha",
            description = "Altera a senha do usuário em endpoint exclusivo, distinto da atualização de dados cadastrais"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Senha alterada com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Senha atual inválida ou dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<Void> updatePassword(
            @Parameter(name = "id", description = "ID do usuário", example = "1")
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdatePasswordRequest request) {
        service.updatePassword(id, request.toCommand());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/type/{typeId}")
    @Operation(summary = "Associar tipo de usuário", description = "Associa um tipo de usuário existente ao usuário informado")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipo de usuário associado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário ou tipo de usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<UserResponse> assignType(
            @Parameter(name = "id", description = "ID do usuário", example = "1")
            @PathVariable("id") Long id,
            @Parameter(name = "typeId", description = "ID do tipo de usuário", example = "1")
            @PathVariable("typeId") Long typeId) {
        return ResponseEntity.ok(UserResponse.from(service.assignType(id, typeId)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Deleta um usuário a partir do ID fornecido")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário deletado com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(name = "id", description = "ID do usuário a ser deletado", example = "1")
            @PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Lista os usuários que estão cadastrados no sistema")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    )
            )
    })
    public ResponseEntity<List<UserResponse>> findAll(
            @Parameter(description = "Filtrar por nome", example = "João")
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(service.findAll(name).stream().map(UserResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário", description = "Busca um usuário a partir do ID fornecido")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    public ResponseEntity<UserResponse> findById(
            @Parameter(name = "id", description = "ID do usuário", example = "1")
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(UserResponse.from(service.findById(id)));
    }
}