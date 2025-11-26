package com.example.restaurant.dto.restaurant;

import com.example.restaurant.entity.KitchenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RestaurantRequestDto(
        @NotBlank(message = "Название ресторана обязательно")
        String name,
        String description,
        @NotNull(message = "Тип кухни обязателен")
        KitchenType kitchenType,
        @NotNull(message = "Средний чек обязателен")
        @Positive(message = "Средний чек должен быть положительным")
        BigDecimal averageCheck
) {
}
