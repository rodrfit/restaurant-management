package com.restaurant.management.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime lastChange;
    private Address address;
    private UserType userType;

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public LocalDateTime getLastChange() { return lastChange; }
    public void setLastChange(LocalDateTime lastChange) { this.lastChange = lastChange; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(name, user.name)
                && Objects.equals(email, user.email)
                && Objects.equals(login, user.login)
                && Objects.equals(password, user.password)
                && Objects.equals(lastChange, user.lastChange)
                && Objects.equals(address, user.address)
                && Objects.equals(userType, user.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, login, password, lastChange, address, userType);
    }
}
