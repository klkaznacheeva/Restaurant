package com.example.restaurant.mapper;

import com.example.restaurant.dto.user.VisitorRequestDto;
import com.example.restaurant.dto.user.VisitorResponseDto;
import com.example.restaurant.entity.Visitor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VisitorMapper {

    private final ModelMapper modelMapper;

    public VisitorMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Visitor toEntity(VisitorRequestDto dto) {
        return modelMapper.map(dto, Visitor.class);
    }

    public VisitorResponseDto toDto(Visitor visitor) {
        return new VisitorResponseDto(
                visitor.getId(),
                visitor.getName(),
                visitor.getAge(),
                visitor.getGender()
        );
    }
}
