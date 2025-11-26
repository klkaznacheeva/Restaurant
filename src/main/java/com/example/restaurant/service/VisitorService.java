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
        Visitor visitor = visitorRepository.findById(id);
        if (visitor == null) {
            throw new IllegalArgumentException("Visitor with id=" + id + " not found");
        }
        return visitorMapper.toDto(visitor);
    }

    public VisitorResponseDto create(VisitorRequestDto dto) {
        Visitor visitor = visitorMapper.toEntity(dto);
        visitorRepository.save(visitor);
        return visitorMapper.toDto(visitor);
    }

    public VisitorResponseDto update(Long id, VisitorRequestDto dto) {
        Visitor existing = visitorRepository.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Visitor with id=" + id + " not found");
        }

        existing.setName(dto.name());
        existing.setAge(dto.age());
        existing.setGender(dto.gender());

        visitorRepository.save(existing);
        return visitorMapper.toDto(existing);
    }

    public void delete(Long id) {
        Visitor existing = visitorRepository.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Visitor with id=" + id + " not found");
        }
        visitorRepository.remove(existing);
    }
}
