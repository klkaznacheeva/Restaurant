package com.example.restaurant.service;

import com.example.restaurant.dto.user.VisitorRequestDto;
import com.example.restaurant.dto.user.VisitorResponseDto;
import com.example.restaurant.entity.Visitor;
import com.example.restaurant.mapper.VisitorMapper;
import com.example.restaurant.repository.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitorServiceTest {

    @Mock
    private VisitorRepository visitorRepository;

    @Mock
    private VisitorMapper visitorMapper;

    @InjectMocks
    private VisitorService visitorService;

    @Test
    void getAll_returnsDtos() {
        Visitor v1 = new Visitor(); v1.setId(1L); v1.setName("Ann"); v1.setAge(20); v1.setGender("Ж");
        Visitor v2 = new Visitor(); v2.setId(2L); v2.setName("Bob"); v2.setAge(25); v2.setGender("М");

        when(visitorRepository.findAll()).thenReturn(List.of(v1, v2));
        when(visitorMapper.toDto(v1)).thenReturn(new VisitorResponseDto(1L, "Ann", 20, "Ж"));
        when(visitorMapper.toDto(v2)).thenReturn(new VisitorResponseDto(2L, "Bob", 25, "М"));

        var result = visitorService.getAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());
        verify(visitorRepository).findAll();
        verify(visitorMapper).toDto(v1);
        verify(visitorMapper).toDto(v2);
    }

    @Test
    void getById_whenNotFound_throws() {
        when(visitorRepository.findById(99L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> visitorService.getById(99L));
        verify(visitorRepository).findById(99L);
        verifyNoInteractions(visitorMapper);
    }

    @Test
    void create_savesAndReturnsDto() {
        var req = new VisitorRequestDto("Ivan", 19, "М");

        Visitor entity = new Visitor();
        when(visitorMapper.toEntity(req)).thenReturn(entity);
        doNothing().when(visitorRepository).save(entity);

        when(visitorMapper.toDto(entity)).thenReturn(new VisitorResponseDto(null, "Ivan", 19, "М"));

        var result = visitorService.create(req);

        assertEquals("Ivan", result.name());
        verify(visitorMapper).toEntity(req);
        verify(visitorRepository).save(entity);
        verify(visitorMapper).toDto(entity);
    }

    @Test
    void update_whenExists_updatesFields_andSaves() {
        var req = new VisitorRequestDto("NewName", 30, "Ж");

        Visitor existing = new Visitor();
        existing.setId(1L);
        existing.setName("Old");
        existing.setAge(10);
        existing.setGender("М");

        when(visitorRepository.findById(1L)).thenReturn(existing);
        doNothing().when(visitorRepository).save(existing);

        when(visitorMapper.toDto(existing)).thenReturn(new VisitorResponseDto(1L, "NewName", 30, "Ж"));

        var result = visitorService.update(1L, req);

        assertEquals(1L, result.id());
        assertEquals("NewName", existing.getName());
        assertEquals(30, existing.getAge());
        assertEquals("Ж", existing.getGender());

        verify(visitorRepository).findById(1L);
        verify(visitorRepository).save(existing);
        verify(visitorMapper).toDto(existing);
    }

    @Test
    void delete_whenExists_removes() {
        Visitor existing = new Visitor();
        existing.setId(1L);

        when(visitorRepository.findById(1L)).thenReturn(existing);
        doNothing().when(visitorRepository).remove(existing);

        visitorService.delete(1L);

        verify(visitorRepository).findById(1L);
        verify(visitorRepository).remove(existing);
    }
}
