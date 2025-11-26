package com.example.restaurant.service;

import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    public void remove(Restaurant restaurant) {
        restaurantRepository.remove(restaurant);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }
}
