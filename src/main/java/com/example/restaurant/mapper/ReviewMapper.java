package com.example.restaurant.mapper;

import com.example.restaurant.dto.review.ReviewRequestDto;
import com.example.restaurant.dto.review.ReviewResponseDto;
import com.example.restaurant.entity.Review;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final ModelMapper modelMapper;

    public ReviewMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Review toEntity(ReviewRequestDto dto) {
        return modelMapper.map(dto, Review.class);
    }

    public ReviewResponseDto toDto(Review review) {
        return new ReviewResponseDto(
                review.getVisitorId(),
                review.getRestaurantId(),
                review.getRating(),
                review.getText()
        );
    }
}
