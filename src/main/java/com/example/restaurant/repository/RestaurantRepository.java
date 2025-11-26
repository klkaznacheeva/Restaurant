package com.example.restaurant.repository;

import com.example.restaurant.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantRepository {

    private final List<Restaurant> restaurants = new ArrayList<>();
    private long sequence = 0L;

    public void save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(++sequence);
            restaurants.add(restaurant);
        } else {
            removeById(restaurant.getId());
            restaurants.add(restaurant);
        }
    }

    public void remove(Restaurant restaurant) {
        restaurants.removeIf(r -> r.getId().equals(restaurant.getId()));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }

    public Restaurant findById(Long id) {
        return restaurants.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void removeById(Long id) {
        restaurants.removeIf(r -> r.getId().equals(id));
    }
}
