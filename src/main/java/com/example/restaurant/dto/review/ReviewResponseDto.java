package com.example.restaurant.dto.review;

public record ReviewResponseDto(
        Long visitorId,
        Long restaurantId,
        int rating,
        String text
) {
}
