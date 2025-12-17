package com.example.restaurant.service;

import com.example.restaurant.dto.restaurant.RestaurantRequestDto;
import com.example.restaurant.dto.restaurant.RestaurantResponseDto;
import com.example.restaurant.entity.KitchenType;
import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.mapper.RestaurantMapper;
import com.example.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void getAll_returnsDtos() {
        Restaurant r1 = new Restaurant();
        r1.setId(1L);

        Restaurant r2 = new Restaurant();
        r2.setId(2L);

        when(restaurantRepository.findAll()).thenReturn(List.of(r1, r2));
        when(restaurantMapper.toDto(r1)).thenReturn(new RestaurantResponseDto(
                1L, "A", "d", KitchenType.РУССКАЯ, new BigDecimal("500"), new BigDecimal("0")
        ));
        when(restaurantMapper.toDto(r2)).thenReturn(new RestaurantResponseDto(
                2L, "B", "d", KitchenType.ИТАЛЬЯНСКАЯ, new BigDecimal("700"), new BigDecimal("0")
        ));

        var result = restaurantService.getAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
        verify(restaurantRepository).findAll();
    }

    @Test
    void getById_whenNotFound_throws() {
        when(restaurantRepository.findById(10L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> restaurantService.getById(10L));
        verify(restaurantRepository).findById(10L);
        verifyNoInteractions(restaurantMapper);
    }

    @Test
    void create_setsRatingZero_saves_andReturnsDto() {
        var req = new RestaurantRequestDto("Uyut", "desc", KitchenType.РУССКАЯ, new BigDecimal("600"));

        Restaurant entity = new Restaurant();
        when(restaurantMapper.toEntity(req)).thenReturn(entity);
        doNothing().when(restaurantRepository).save(entity);

        when(restaurantMapper.toDto(entity)).thenReturn(new RestaurantResponseDto(
                null, "Uyut", "desc", KitchenType.РУССКАЯ, new BigDecimal("600"), BigDecimal.ZERO
        ));

        var result = restaurantService.create(req);

        assertEquals(BigDecimal.ZERO, entity.getRating());
        assertEquals("Uyut", result.name());
        verify(restaurantRepository).save(entity);
    }

    @Test
    void update_whenExists_updatesFields_andSaves() {
        var req = new RestaurantRequestDto("New", "newDesc", KitchenType.ЯПОНСКАЯ, new BigDecimal("900"));

        Restaurant existing = new Restaurant();
        existing.setId(5L);
        existing.setName("Old");
        existing.setDescription("old");
        existing.setKitchenType(KitchenType.РУССКАЯ);
        existing.setAverageCheck(new BigDecimal("100"));

        when(restaurantRepository.findById(5L)).thenReturn(existing);
        doNothing().when(restaurantRepository).save(existing);

        when(restaurantMapper.toDto(existing)).thenReturn(new RestaurantResponseDto(
                5L, "New", "newDesc", KitchenType.ЯПОНСКАЯ, new BigDecimal("900"), existing.getRating()
        ));

        var result = restaurantService.update(5L, req);

        assertEquals(5L, result.id());
        assertEquals("New", existing.getName());
        assertEquals("newDesc", existing.getDescription());
        assertEquals(KitchenType.ЯПОНСКАЯ, existing.getKitchenType());
        assertEquals(new BigDecimal("900"), existing.getAverageCheck());

        verify(restaurantRepository).save(existing);
    }

    @Test
    void delete_whenExists_removes() {
        Restaurant existing = new Restaurant();
        existing.setId(7L);

        when(restaurantRepository.findById(7L)).thenReturn(existing);
        doNothing().when(restaurantRepository).remove(existing);

        restaurantService.delete(7L);

        verify(restaurantRepository).findById(7L);
        verify(restaurantRepository).remove(existing);
    }
}
