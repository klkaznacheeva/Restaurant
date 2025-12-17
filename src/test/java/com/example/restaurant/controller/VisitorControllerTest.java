package com.example.restaurant.controller;

import com.example.restaurant.dto.user.VisitorRequestDto;
import com.example.restaurant.dto.user.VisitorResponseDto;
import com.example.restaurant.service.VisitorService;
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

@WebMvcTest(VisitorController.class)
class VisitorControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    VisitorService visitorService;

    @Test
    void getAll_shouldReturn200_andList() throws Exception {
        when(visitorService.getAll()).thenReturn(List.of(
                new VisitorResponseDto(1L, "Ivan", 19, "М")
        ));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Ivan"));
    }

    @Test
    void getById_shouldReturn200() throws Exception {
        when(visitorService.getById(1L)).thenReturn(new VisitorResponseDto(1L, "Ivan", 19, "М"));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.gender").value("М"));
    }

    @Test
    void post_shouldReturn200() throws Exception {
        var req = new VisitorRequestDto("Ivan", 19, "М");
        when(visitorService.create(any(VisitorRequestDto.class)))
                .thenReturn(new VisitorResponseDto(1L, "Ivan", 19, "М"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void put_shouldReturn200() throws Exception {
        var req = new VisitorRequestDto("New", 20, "Ж");
        when(visitorService.update(eq(1L), any(VisitorRequestDto.class)))
                .thenReturn(new VisitorResponseDto(1L, "New", 20, "Ж"));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void delete_shouldReturn200() throws Exception {
        doNothing().when(visitorService).delete(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(visitorService).delete(1L);
    }
}
