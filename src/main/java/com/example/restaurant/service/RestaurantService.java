package com.example.restaurant.service;

import com.example.restaurant.dto.restaurant.RestaurantRequestDto;
import com.example.restaurant.dto.restaurant.RestaurantResponseDto;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.mapper.RestaurantMapper;
import com.example.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public List<RestaurantResponseDto> getAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    public RestaurantResponseDto getById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id);
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant with id=" + id + " not found");
        }
        return restaurantMapper.toDto(restaurant);
    }

    public RestaurantResponseDto create(RestaurantRequestDto dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        restaurant.setRating(BigDecimal.ZERO);
        restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    public RestaurantResponseDto update(Long id, RestaurantRequestDto dto) {
        Restaurant existing = restaurantRepository.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Restaurant with id=" + id + " not found");
        }

        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setKitchenType(dto.kitchenType());
        existing.setAverageCheck(dto.averageCheck());

        restaurantRepository.save(existing);
        return restaurantMapper.toDto(existing);
    }

    public void delete(Long id) {
        Restaurant existing = restaurantRepository.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Restaurant with id=" + id + " not found");
        }
        restaurantRepository.remove(existing);
    }
}
