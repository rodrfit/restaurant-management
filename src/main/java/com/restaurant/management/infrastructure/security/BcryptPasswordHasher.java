package com.restaurant.management.infrastructure.security;

import com.restaurant.management.domain.port.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordHasher implements PasswordHasher {
    private static final int BCRYPT_STRENGTH = 12;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BcryptPasswordHasher() {
        this(new BCryptPasswordEncoder(BCRYPT_STRENGTH));
    }

    public BcryptPasswordHasher(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null || rawPassword.isBlank() || encodedPassword.isBlank()) {
            return false;
        }
        try {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
