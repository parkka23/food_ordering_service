package com.example.demo2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dish {
    @Id
    private Long id;
    private String name;
    private String type;
    private BigDecimal price;
    private Restaurant restaurant;
}
