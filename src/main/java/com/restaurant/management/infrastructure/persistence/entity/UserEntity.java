package com.restaurant.management.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(name = "last_change")
    private LocalDateTime lastChange;

    @Embedded
    private AddressEmbeddable address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_type_id")
    private UserTypeEntity userType;

    public UserEntity() {}

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
    public AddressEmbeddable getAddress() { return address; }
    public void setAddress(AddressEmbeddable address) { this.address = address; }
    public UserTypeEntity getUserType() { return userType; }
    public void setUserType(UserTypeEntity userType) { this.userType = userType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(email, that.email)
                && Objects.equals(login, that.login)
                && Objects.equals(password, that.password)
                && Objects.equals(lastChange, that.lastChange)
                && Objects.equals(address, that.address)
                && Objects.equals(userType, that.userType);
    }

    @Override
    public int hashCode() { return Objects.hash(id, name, email, login, password, lastChange, address, userType); }
}
