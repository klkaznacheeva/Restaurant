package com.example.restaurant.controller;

import com.example.restaurant.dto.review.ReviewRequestDto;
import com.example.restaurant.dto.review.ReviewResponseDto;
import com.example.restaurant.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewResponseDto> getAll() {
        return reviewService.getAll();
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    public ReviewResponseDto getByIds(@PathVariable Long visitorId,
                                      @PathVariable Long restaurantId) {
        return reviewService.getByIds(visitorId, restaurantId);
    }

    @PostMapping
    public ReviewResponseDto create(@Valid @RequestBody ReviewRequestDto requestDto) {
        return reviewService.create(requestDto);
    }

    @PutMapping("/{visitorId}/{restaurantId}")
    public ReviewResponseDto update(@PathVariable Long visitorId,
                                    @PathVariable Long restaurantId,
                                    @Valid @RequestBody ReviewRequestDto requestDto) {
        return reviewService.update(visitorId, restaurantId, requestDto);
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    public void delete(@PathVariable Long visitorId,
                       @PathVariable Long restaurantId) {
        reviewService.delete(visitorId, restaurantId);
    }
}
