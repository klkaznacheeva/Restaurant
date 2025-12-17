package com.example.restaurant.service;

import com.example.restaurant.dto.review.ReviewRequestDto;
import com.example.restaurant.dto.review.ReviewResponseDto;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.entity.Review;
import com.example.restaurant.mapper.ReviewMapper;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void getByIds_whenNotFound_throws() {
        when(reviewRepository.findById(1L, 2L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> reviewService.getByIds(1L, 2L));
        verify(reviewRepository).findById(1L, 2L);
        verifyNoInteractions(reviewMapper);
    }

    @Test
    void create_savesReview_andRecalculatesRestaurantRating() {
        var dto = new ReviewRequestDto(1L, 10L, 5, "ok");

        Review reviewEntity = new Review();
        reviewEntity.setVisitorId(1L);
        reviewEntity.setRestaurantId(10L);
        reviewEntity.setRating(5);
        reviewEntity.setText("ok");

        when(reviewMapper.toEntity(dto)).thenReturn(reviewEntity);
        doNothing().when(reviewRepository).save(reviewEntity);

        when(reviewRepository.findAll()).thenReturn(List.of(reviewEntity));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        restaurant.setRating(BigDecimal.ZERO);

        when(restaurantRepository.findById(10L)).thenReturn(restaurant);
        doNothing().when(restaurantRepository).save(any(Restaurant.class));

        when(reviewMapper.toDto(reviewEntity)).thenReturn(new ReviewResponseDto(1L, 10L, 5, "ok"));

        var result = reviewService.create(dto);

        assertEquals(1L, result.visitorId());
        assertEquals(10L, result.restaurantId());

        ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository).save(captor.capture());
        assertEquals(new BigDecimal("5.00"), captor.getValue().getRating());
    }

    @Test
    void update_whenExists_updatesRatingText_andRecalculates() {
        var dto = new ReviewRequestDto(1L, 10L, 4, "upd");

        Review existing = new Review();
        existing.setVisitorId(1L);
        existing.setRestaurantId(10L);
        existing.setRating(2);
        existing.setText("old");

        when(reviewRepository.findById(1L, 10L)).thenReturn(existing);
        doNothing().when(reviewRepository).save(existing);

        when(reviewRepository.findAll()).thenReturn(List.of(existing));

        Restaurant restaurant = new Restaurant();
        restaurant.setId(10L);
        when(restaurantRepository.findById(10L)).thenReturn(restaurant);
        doNothing().when(restaurantRepository).save(any(Restaurant.class));

        when(reviewMapper.toDto(existing)).thenReturn(new ReviewResponseDto(1L, 10L, 4, "upd"));

        var result = reviewService.update(1L, 10L, dto);

        assertEquals(4, existing.getRating());
        assertEquals("upd", existing.getText());
        assertEquals(4, result.rating());

        verify(reviewRepository).save(existing);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void delete_whenExists_removes_andRecalculates() {
        Review existing = new Review();
        existing.setVisitorId(1L);
        existing.setRestaurantId(10L);
        existing.setRating(5);

        when(reviewRepository.findById(1L, 10L)).thenReturn(existing);
        doNothing().when(reviewRepository).remove(existing);

        when(reviewRepository.findAll()).thenReturn(List.of());

        reviewService.delete(1L, 10L);

        verify(reviewRepository).remove(existing);
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }
}
