package com.example.restaurant.service;

import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.entity.Review;
import com.example.restaurant.entity.Visitor;
import com.example.restaurant.service.RestaurantService;
import com.example.restaurant.service.ReviewService;
import com.example.restaurant.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements CommandLineRunner {

    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @Override
    public void run(String... args) {
        System.out.println("=== CommandLineRunner: дополнительная проверка сервиса ===");

        List<Visitor> visitors = visitorService.findAll();
        List<Restaurant> restaurants = restaurantService.findAll();
        List<Review> reviews = reviewService.findAll();

        System.out.println("Посетители:");
        visitorService.findAll().forEach(v ->
                System.out.println(" - " + v.getId() + ": " + v.getName())
        );

        System.out.println("Рестораны:");
        restaurantService.findAll().forEach(r ->
                System.out.println(" - " + r.getId() + ": " + r.getName() + " (рейтинг: " + r.getRating() + ")")
        );

        System.out.println("--- Отзывы ---");
        for (Review review : reviews) {
            String visitorName = visitors.stream()
                    .filter(v -> v.getId().equals(review.getVisitorId()))
                    .map(v -> v.getName() != null ? v.getName() : "Аноним")
                    .findFirst()
                    .orElse("Неизвестный посетитель");

            String restaurantName = restaurants.stream()
                    .filter(r -> r.getId().equals(review.getRestaurantId()))
                    .map(Restaurant::getName)
                    .findFirst()
                    .orElse("Неизвестный ресторан");

            String text = review.getText() != null
                    ? review.getText()
                    : "(без текста)";

            System.out.printf(
                    "Ресторан: %s | Посетитель: %s | Оценка: %d | Отзыв: %s%n",
                    restaurantName,
                    visitorName,
                    review.getRating(),
                    text
            );
        }

        System.out.println("=== Конец CommandLineRunner ===\n");
    }
}
