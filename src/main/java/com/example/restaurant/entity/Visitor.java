package com.example.restaurant.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {

    private Long id;
    private String name;
    private int age;
    private String gender;
}
