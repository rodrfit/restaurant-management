package com.restaurant.management.domain.model;

import java.util.Objects;

public class UserType {
    private Long id;
    private String name;
    private String observation;

    public UserType() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserType)) return false;
        UserType userType = (UserType) o;
        return Objects.equals(id, userType.id)
                && Objects.equals(name, userType.name)
                && Objects.equals(observation, userType.observation);
    }

    @Override
    public int hashCode() { return Objects.hash(id, name, observation); }
}
