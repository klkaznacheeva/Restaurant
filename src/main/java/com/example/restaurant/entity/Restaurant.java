package com.example.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "kitchen_type", nullable = false)
    private KitchenType kitchenType;

    @Column(name = "average_check", nullable = false)
    private BigDecimal averageCheck;

    @Column(precision = 4, scale = 2)
    private BigDecimal rating;
}
