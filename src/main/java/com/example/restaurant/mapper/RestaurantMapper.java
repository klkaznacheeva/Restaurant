package com.example.restaurant.mapper;

import com.example.restaurant.dto.restaurant.RestaurantRequestDto;
import com.example.restaurant.dto.restaurant.RestaurantResponseDto;
import com.example.restaurant.entity.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    private final ModelMapper modelMapper;

    public RestaurantMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Restaurant toEntity(RestaurantRequestDto dto) {
        return modelMapper.map(dto, Restaurant.class);
    }

    public RestaurantResponseDto toDto(Restaurant restaurant) {
        return new RestaurantResponseDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getKitchenType(),
                restaurant.getAverageCheck(),
                restaurant.getRating()
        );
    }
}
