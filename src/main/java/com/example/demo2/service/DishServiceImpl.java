package com.example.demo2.service;

import com.example.demo2.dto.DishDto;
import com.example.demo2.entity.Dish;
import com.example.demo2.repository.DishRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DishServiceImpl {
    @Autowired
    private final DishRepositoryImpl dishRepository;

    public DishServiceImpl(DishRepositoryImpl dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<DishDto> getAll() {
        return dishRepository.findAll();

    }

    public Dish create(Dish dish) {
        dishRepository.save(dish);
        return dish;
    }

    public int delete(Long id) {
        return dishRepository.deleteById(id);
    }

    public DishDto getById(Long id) {
        return dishRepository.findById(id);
    }
}
