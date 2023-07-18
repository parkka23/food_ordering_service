package com.example.demo2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishDto {
    private Long id;
    private String name;
    private String type;
    private BigDecimal price;
    private Long restaurant_id;
}
