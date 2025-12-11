package com.example.restaurant.service;

import com.example.restaurant.dto.user.VisitorRequestDto;
import com.example.restaurant.dto.user.VisitorResponseDto;
import com.example.restaurant.entity.Visitor;
import com.example.restaurant.mapper.VisitorMapper;
import com.example.restaurant.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;
    private final VisitorMapper visitorMapper;

    public List<VisitorResponseDto> getAll() {
        return visitorRepository.findAll().stream()
                .map(visitorMapper::toDto)
                .toList();
    }

    public VisitorResponseDto getById(Long id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visitor with id=" + id + " not found"));
        return visitorMapper.toDto(visitor);
    }

    public VisitorResponseDto create(VisitorRequestDto dto) {
        Visitor visitor = visitorMapper.toEntity(dto);
        Visitor saved = visitorRepository.save(visitor);
        return visitorMapper.toDto(saved);
    }

    public VisitorResponseDto update(Long id, VisitorRequestDto dto) {
        Visitor existing = visitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visitor with id=" + id + " not found"));

        existing.setName(dto.name());
        existing.setAge(dto.age());
        existing.setGender(dto.gender());

        Visitor saved = visitorRepository.save(existing);
        return visitorMapper.toDto(saved);
    }

    public void delete(Long id) {
        Visitor existing = visitorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Visitor with id=" + id + " not found"));
        visitorRepository.delete(existing);
    }
}
