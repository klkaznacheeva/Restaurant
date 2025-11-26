package com.example.restaurant.dto.restaurant;

import com.example.restaurant.entity.KitchenType;

import java.math.BigDecimal;

public record RestaurantResponseDto(
        Long id,
        String name,
        String description,
        KitchenType kitchenType,
        BigDecimal averageCheck,
        BigDecimal rating
) {
}
