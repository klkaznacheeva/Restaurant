package com.example.restaurant.controller;

import com.example.restaurant.dto.restaurant.RestaurantRequestDto;
import com.example.restaurant.dto.restaurant.RestaurantResponseDto;
import com.example.restaurant.entity.KitchenType;
import com.example.restaurant.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    RestaurantService restaurantService;

    @Test
    void getAll_shouldReturn200() throws Exception {
        when(restaurantService.getAll()).thenReturn(List.of(
                new RestaurantResponseDto(1L, "Uyut", "desc", KitchenType.РУССКАЯ, new BigDecimal("600"), new BigDecimal("0"))
        ));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].kitchenType").value("РУССКАЯ"));
    }

    @Test
    void getById_shouldReturn200() throws Exception {
        when(restaurantService.getById(1L)).thenReturn(
                new RestaurantResponseDto(1L, "Uyut", "desc", KitchenType.РУССКАЯ, new BigDecimal("600"), new BigDecimal("4.50"))
        );

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Uyut"))
                .andExpect(jsonPath("$.rating").value(4.50));
    }

    @Test
    void post_shouldReturn200() throws Exception {
        var req = new RestaurantRequestDto("Uyut", "desc", KitchenType.РУССКАЯ, new BigDecimal("600"));

        when(restaurantService.create(any(RestaurantRequestDto.class))).thenReturn(
                new RestaurantResponseDto(1L, "Uyut", "desc", KitchenType.РУССКАЯ, new BigDecimal("600"), BigDecimal.ZERO)
        );

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void put_shouldReturn200() throws Exception {
        var req = new RestaurantRequestDto("New", "new", KitchenType.ЯПОНСКАЯ, new BigDecimal("900"));

        when(restaurantService.update(eq(1L), any(RestaurantRequestDto.class))).thenReturn(
                new RestaurantResponseDto(1L, "New", "new", KitchenType.ЯПОНСКАЯ, new BigDecimal("900"), new BigDecimal("0"))
        );

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.kitchenType").value("ЯПОНСКАЯ"));
    }

    @Test
    void delete_shouldReturn200() throws Exception {
        doNothing().when(restaurantService).delete(1L);

        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isOk());

        verify(restaurantService).delete(1L);
    }
}
