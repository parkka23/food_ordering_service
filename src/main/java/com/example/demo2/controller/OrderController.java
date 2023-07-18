package com.example.demo2.controller;

import com.example.demo2.service.OrderServiceImpl;
import com.example.demo2.service.DishServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private final OrderServiceImpl orderService;

    @Autowired
    private final DishServiceImpl dishService;


    public OrderController(OrderServiceImpl orderService, DishServiceImpl dishService) {
        this.orderService = orderService;
        this.dishService = dishService;
    }

}