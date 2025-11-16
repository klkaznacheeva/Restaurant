package com.example.restaurant.service;

import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.entity.Review;
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

    public void save(Review review) {
        reviewRepository.save(review);
        recalculateRestaurantRating(review.getRestaurantId());
    }

    public void remove(Review review) {
        reviewRepository.remove(review);
        recalculateRestaurantRating(review.getRestaurantId());
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
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

        Restaurant restaurant = restaurantRepository.findAll().stream()
                .filter(r -> r.getId().equals(restaurantId))
                .findFirst()
                .orElse(null);

        if (restaurant != null) {
            restaurant.setRating(
                    BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP)
            );
        }
    }
}
