package com.restaurant.management.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BcryptPasswordHasher")
class BcryptPasswordHasherTest {

    private final BcryptPasswordHasher hasher = new BcryptPasswordHasher(new BCryptPasswordEncoder());

    @Nested
    @DisplayName("quando a senha é validada")
    class MatchingTests {

        @Test
        @DisplayName("deve retornar true para senha válida e hash correspondente")
        void shouldReturnTrueWhenPasswordMatchesHash() {
            String encoded = hasher.hash("senha123");

            assertTrue(hasher.matches("senha123", encoded));
        }

        @Test
        @DisplayName("deve retornar false quando a senha é nula")
        void shouldReturnFalseWhenRawPasswordIsNull() {
            assertFalse(hasher.matches(null, hasher.hash("senha123")));
        }

        @Test
        @DisplayName("deve retornar false quando a senha é em branco")
        void shouldReturnFalseWhenRawPasswordIsBlank() {
            assertFalse(hasher.matches("   ", hasher.hash("senha123")));
        }

        @Test
        @DisplayName("deve retornar false quando o hash é vazio ou em branco")
        void shouldReturnFalseWhenEncodedPasswordIsBlank() {
            assertFalse(hasher.matches("senha123", "   "));
        }

        @Test
        @DisplayName("deve retornar false quando a senha não corresponde ao hash")
        void shouldReturnFalseWhenPasswordDoesNotMatchHash() {
            assertFalse(hasher.matches("outraSenha", hasher.hash("senha123")));
        }

        @Test
        @DisplayName("deve retornar false quando o hash armazenado for inválido")
        void shouldReturnFalseWhenEncodedPasswordIsInvalid() {
            assertFalse(hasher.matches("senha123", "hash-invalido"));
        }
    }

    @Nested
    @DisplayName("quando o hash é gerado")
    class HashingTests {

        @Test
        @DisplayName("deve gerar um hash bcrypt diferente da senha em texto puro")
        void shouldGenerateBcryptHash() {
            String hashed = hasher.hash("senha123");

            assertNotNull(hashed);
            assertNotEquals("senha123", hashed);
            assertTrue(hashed.startsWith("$2a$") || hashed.startsWith("$2b$") || hashed.startsWith("$2y$"));
        }
    }
}
