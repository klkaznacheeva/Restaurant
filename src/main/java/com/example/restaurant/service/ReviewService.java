package com.example.restaurant.service;

import com.example.restaurant.dto.review.ReviewRequestDto;
import com.example.restaurant.dto.review.ReviewResponseDto;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.entity.Review;
import com.example.restaurant.mapper.ReviewMapper;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewResponseDto> getAll() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    public ReviewResponseDto getByIds(Long visitorId, Long restaurantId) {
        Review review = reviewRepository.findById(visitorId, restaurantId);
        if (review == null) {
            throw new IllegalArgumentException(
                    "Review not found for visitorId=" + visitorId + ", restaurantId=" + restaurantId
            );
        }
        return reviewMapper.toDto(review);
    }

    public ReviewResponseDto create(ReviewRequestDto dto) {
        Review review = reviewMapper.toEntity(dto);
        reviewRepository.save(review);
        recalculateRestaurantRating(dto.restaurantId());
        return reviewMapper.toDto(review);
    }

    public ReviewResponseDto update(Long visitorId, Long restaurantId, ReviewRequestDto dto) {
        Review existing = reviewRepository.findById(visitorId, restaurantId);
        if (existing == null) {
            throw new IllegalArgumentException(
                    "Review not found for visitorId=" + visitorId + ", restaurantId=" + restaurantId
            );
        }

        existing.setRating(dto.rating());
        existing.setText(dto.text());
        reviewRepository.save(existing);

        recalculateRestaurantRating(restaurantId);
        return reviewMapper.toDto(existing);
    }

    public void delete(Long visitorId, Long restaurantId) {
        Review existing = reviewRepository.findById(visitorId, restaurantId);
        if (existing == null) {
            throw new IllegalArgumentException(
                    "Review not found for visitorId=" + visitorId + ", restaurantId=" + restaurantId
            );
        }
        reviewRepository.remove(existing);
        recalculateRestaurantRating(restaurantId);
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        List<Review> restaurantReviews = reviewRepository.findAll().stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .toList();

        if (restaurantReviews.isEmpty()) {
            return;
        }

        double avg = restaurantReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant != null) {
            restaurant.setRating(
                    BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP)
            );
            restaurantRepository.save(restaurant);
        }
    }
}
