package com.greta.productShop.dto;

public record UserDto(
        int id,
        String email,
        String name,
        String firstName,
        String address,
        String phoneNumber,
        String postalNumber,
        String role
) {}
