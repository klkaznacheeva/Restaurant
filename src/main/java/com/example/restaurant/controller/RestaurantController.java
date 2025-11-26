package com.example.restaurant.controller;

import com.example.restaurant.dto.restaurant.RestaurantRequestDto;
import com.example.restaurant.dto.restaurant.RestaurantResponseDto;
import com.example.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public List<RestaurantResponseDto> getAll() {
        return restaurantService.getAll();
    }

    @GetMapping("/{id}")
    public RestaurantResponseDto getById(@PathVariable Long id) {
        return restaurantService.getById(id);
    }

    @PostMapping
    public RestaurantResponseDto create(@Valid @RequestBody RestaurantRequestDto requestDto) {
        return restaurantService.create(requestDto);
    }

    @PutMapping("/{id}")
    public RestaurantResponseDto update(@PathVariable Long id,
                                        @Valid @RequestBody RestaurantRequestDto requestDto) {
        return restaurantService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        restaurantService.delete(id);
    }
}
