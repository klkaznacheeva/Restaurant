package com.example.restaurant.controller;

import com.example.restaurant.dto.user.VisitorRequestDto;
import com.example.restaurant.dto.user.VisitorResponseDto;
import com.example.restaurant.service.VisitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    @GetMapping
    public List<VisitorResponseDto> getAll() {
        return visitorService.getAll();
    }

    @GetMapping("/{id}")
    public VisitorResponseDto getById(@PathVariable Long id) {
        return visitorService.getById(id);
    }

    @PostMapping
    public VisitorResponseDto create(@Valid @RequestBody VisitorRequestDto requestDto) {
        return visitorService.create(requestDto);
    }

    @PutMapping("/{id}")
    public VisitorResponseDto update(@PathVariable Long id,
                                     @Valid @RequestBody VisitorRequestDto requestDto) {
        return visitorService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        visitorService.delete(id);
    }
}
