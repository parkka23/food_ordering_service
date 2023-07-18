package com.example.demo2.entity;

import com.example.demo2.dto.DishDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    @Id
    private Long id;
    private String name;
    private String description;
    private List<DishDto> dishes;
}
