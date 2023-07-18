package com.example.demo2.service;

import com.example.demo2.entity.Order;
import com.example.demo2.repository.OrderRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl  {
    @Autowired
    private final OrderRepositoryImpl orderRepository;

    public OrderServiceImpl(OrderRepositoryImpl orderRepository) {
        this.orderRepository = orderRepository;
    }
    public Order create(Order order) {
        orderRepository.save(order);
        return order;
    }

}
