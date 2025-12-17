package com.example.restaurant.controller;

import com.example.restaurant.dto.review.ReviewRequestDto;
import com.example.restaurant.dto.review.ReviewResponseDto;
import com.example.restaurant.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    ReviewService reviewService;

    @Test
    void getAll_shouldReturn200() throws Exception {
        when(reviewService.getAll()).thenReturn(List.of(
                new ReviewResponseDto(1L, 10L, 5, "ok")
        ));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorId").value(1))
                .andExpect(jsonPath("$[0].restaurantId").value(10));
    }

    @Test
    void getByIds_shouldReturn200() throws Exception {
        when(reviewService.getByIds(1L, 10L)).thenReturn(new ReviewResponseDto(1L, 10L, 5, "ok"));

        mockMvc.perform(get("/api/reviews/1/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.text").value("ok"));
    }

    @Test
    void post_shouldReturn200() throws Exception {
        var req = new ReviewRequestDto(1L, 10L, 5, "ok");
        when(reviewService.create(any(ReviewRequestDto.class))).thenReturn(new ReviewResponseDto(1L, 10L, 5, "ok"));

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void put_shouldReturn200() throws Exception {
        var req = new ReviewRequestDto(1L, 10L, 4, "upd");
        when(reviewService.update(eq(1L), eq(10L), any(ReviewRequestDto.class)))
                .thenReturn(new ReviewResponseDto(1L, 10L, 4, "upd"));

        mockMvc.perform(put("/api/reviews/1/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("upd"));
    }

    @Test
    void delete_shouldReturn200() throws Exception {
        doNothing().when(reviewService).delete(1L, 10L);

        mockMvc.perform(delete("/api/reviews/1/10"))
                .andExpect(status().isOk());

        verify(reviewService).delete(1L, 10L);
    }
}
