package com.restaurant.management.application.command;

public record AddressData(
        String street,
        String number,
        String city,
        String zipCode
) {
}
