package com.example.restaurant.service;

import com.example.restaurant.dto.review.ReviewRequestDto;
import com.example.restaurant.dto.review.ReviewResponseDto;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.entity.Review;
import com.example.restaurant.entity.Visitor;
import com.example.restaurant.mapper.ReviewMapper;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.ReviewRepository;
import com.example.restaurant.repository.VisitorRepository;
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
    private final VisitorRepository visitorRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewResponseDto> getAll() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    public ReviewResponseDto getByIds(Long visitorId, Long restaurantId) {
        Review review = reviewRepository.findByVisitor_IdAndRestaurant_Id(visitorId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Review not found for visitorId=" + visitorId + ", restaurantId=" + restaurantId
                ));
        return reviewMapper.toDto(review);
    }

    public ReviewResponseDto create(ReviewRequestDto dto) {
        Visitor visitor = visitorRepository.findById(dto.visitorId())
                .orElseThrow(() -> new IllegalArgumentException("Visitor with id=" + dto.visitorId() + " not found"));

        Restaurant restaurant = restaurantRepository.findById(dto.restaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant with id=" + dto.restaurantId() + " not found"));

        Review review = new Review();
        review.setVisitor(visitor);
        review.setRestaurant(restaurant);
        review.setRating(dto.rating());
        review.setText(dto.text());

        Review saved = reviewRepository.save(review);
        recalculateRestaurantRating(restaurant.getId());
        return reviewMapper.toDto(saved);
    }

    public ReviewResponseDto update(Long visitorId, Long restaurantId, ReviewRequestDto dto) {
        Review existing = reviewRepository.findByVisitor_IdAndRestaurant_Id(visitorId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Review not found for visitorId=" + visitorId + ", restaurantId=" + restaurantId
                ));

        existing.setRating(dto.rating());
        existing.setText(dto.text());
        Review saved = reviewRepository.save(existing);

        recalculateRestaurantRating(restaurantId);
        return reviewMapper.toDto(saved);
    }

    public void delete(Long visitorId, Long restaurantId) {
        Review existing = reviewRepository.findByVisitor_IdAndRestaurant_Id(visitorId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Review not found for visitorId=" + visitorId + ", restaurantId=" + restaurantId
                ));

        reviewRepository.delete(existing);
        recalculateRestaurantRating(restaurantId);
    }

    private void recalculateRestaurantRating(Long restaurantId) {
        List<Review> restaurantReviews = reviewRepository.findAllByRestaurant_Id(restaurantId);

        if (restaurantReviews.isEmpty()) {
            restaurantRepository.findById(restaurantId).ifPresent(r -> {
                r.setRating(null);
                restaurantRepository.save(r);
            });
            return;
        }

        double avg = restaurantReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        restaurantRepository.findById(restaurantId).ifPresent(restaurant -> {
            restaurant.setRating(
                    BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP)
            );
            restaurantRepository.save(restaurant);
        });
    }
}
