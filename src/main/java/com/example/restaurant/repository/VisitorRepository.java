package com.example.restaurant.repository;

import com.example.restaurant.entity.Visitor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitorRepository {

    private final List<Visitor> visitors = new ArrayList<>();
    private long sequence = 0L;

    public void save(Visitor visitor) {
        if (visitor.getId() == null) {
            visitor.setId(++sequence);
            visitors.add(visitor);
        } else {
            removeById(visitor.getId());
            visitors.add(visitor);
        }
    }

    public void remove(Visitor visitor) {
        visitors.removeIf(v -> v.getId().equals(visitor.getId()));
    }

    public List<Visitor> findAll() {
        return new ArrayList<>(visitors);
    }

    public Visitor findById(Long id) {
        return visitors.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private void removeById(Long id) {
        visitors.removeIf(v -> v.getId().equals(id));
    }
}
