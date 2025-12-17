package com.example.restaurant.repository;

import com.example.restaurant.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByVisitor_IdAndRestaurant_Id(Long visitorId, Long restaurantId);

    List<Review> findAllByRestaurant_Id(Long restaurantId);
}
