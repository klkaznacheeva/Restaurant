package com.example.restaurant.mapper;

import com.example.restaurant.dto.review.ReviewResponseDto;
import com.example.restaurant.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(
                review.getVisitor().getId(),
                review.getRestaurant().getId(),
                review.getRating(),
                review.getText()
        );
    }
}
