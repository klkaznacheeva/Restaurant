package com.example.restaurant.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VisitorRequestDto(
        @Size(max = 100, message = "Имя не должно быть длиннее 100 символов")
        String name,
        @Min(value = 1, message = "Возраст должен быть не меньше 1")
        @Max(value = 120, message = "Возраст должен быть не больше 120")
        int age,
        @NotBlank(message = "Пол обязателен")
        String gender
) {
}
