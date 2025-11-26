package com.example.restaurant.dto.user;

public record VisitorResponseDto(
        Long id,
        String name,
        int age,
        String gender
) {
}
