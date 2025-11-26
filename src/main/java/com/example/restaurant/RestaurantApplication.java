package com.example.restaurant;

import com.example.restaurant.entity.KitchenType;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.entity.Review;
import com.example.restaurant.entity.Visitor;
import com.example.restaurant.service.RestaurantService;
import com.example.restaurant.service.ReviewService;
import com.example.restaurant.service.VisitorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;

@SpringBootApplication
@RequiredArgsConstructor
public class RestaurantApplication {

	private final VisitorService visitorService;
	private final RestaurantService restaurantService;
	private final ReviewService reviewService;

	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}

	@PostConstruct
	public void initData() {
		System.out.println("=== Инициализация тестовых данных (@PostConstruct) ===");

		Visitor v1 = new Visitor(1L, "Анна", 25, "Ж");
		Visitor v2 = new Visitor(2L, null, 30, "М");
		Visitor v3 = new Visitor(3L, "Иван", 19, "М");

		visitorService.save(v1);
		visitorService.save(v2);
		visitorService.save(v3);

		Restaurant r1 = new Restaurant(
				1L,
				"Итальянский дворик",
				"Пицца, паста, домашняя атмосфера",
				KitchenType.ИТАЛЬЯНСКАЯ,
				new BigDecimal("800"),
				BigDecimal.ZERO
		);

		Restaurant r2 = new Restaurant(
				2L,
				"Азия",
				"Аутентичные блюда китайской кухни",
				KitchenType.КИТАЙСКАЯ,
				new BigDecimal("600"),
				BigDecimal.ZERO
		);

		restaurantService.save(r1);
		restaurantService.save(r2);

		Review review1 = new Review(1L, 1L, 5, "Очень вкусно и уютно!");
		Review review2 = new Review(2L, 1L, 4, "Хорошо, но немного шумно.");
		Review review3 = new Review(1L, 2L, 3, "Ожидала большего, но в целом неплохо.");
		Review review4 = new Review(3L, 2L, 5, null);

		reviewService.save(review1);
		reviewService.save(review2);
		reviewService.save(review3);
		reviewService.save(review4);

		printAllData();
	}

	private void printAllData() {
		System.out.println();
		System.out.println("\n--- Посетители ---");
		visitorService.findAll().forEach(visitor ->
				System.out.printf("id=%d, name=%s, age=%d, gender=%s%n",
						visitor.getId(),
						visitor.getName() != null ? visitor.getName() : "Аноним",
						visitor.getAge(),
						visitor.getGender())
		);

		System.out.println("\n--- Рестораны (с рейтингами) ---");
		restaurantService.findAll().forEach(restaurant ->
				System.out.printf("id=%d, name=%s, kitchen=%s, avgCheck=%s, rating=%s%n",
						restaurant.getId(),
						restaurant.getName(),
						restaurant.getKitchenType(),
						restaurant.getAverageCheck(),
						restaurant.getRating())
		);

		System.out.println("\n--- Все отзывы (сырые данные) ---");
		reviewService.findAll().forEach(System.out::println);

		System.out.println("===================================================\n");
	}
}
