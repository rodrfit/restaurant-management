package com.restaurant.management.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class User {

    private Long id;

    private String name;

    private String email;

    private String login;

    private String password;

    private LocalDateTime lastChange;

    private Address address;

    private UserType userType;
}
