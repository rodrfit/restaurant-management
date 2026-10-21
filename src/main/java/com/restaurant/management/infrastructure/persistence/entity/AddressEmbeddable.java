package com.restaurant.management.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class AddressEmbeddable {
    private String street;
    private String number;
    private String city;
    private String zipCode;

    public AddressEmbeddable() {}

    public AddressEmbeddable(String street, String number, String city, String zipCode) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.zipCode = zipCode;
    }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressEmbeddable)) return false;
        AddressEmbeddable that = (AddressEmbeddable) o;
        return Objects.equals(street, that.street)
                && Objects.equals(number, that.number)
                && Objects.equals(city, that.city)
                && Objects.equals(zipCode, that.zipCode);
    }

    @Override
    public int hashCode() { return Objects.hash(street, number, city, zipCode); }
}
