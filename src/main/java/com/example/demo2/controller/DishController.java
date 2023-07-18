package com.example.demo2.controller;

import com.example.demo2.dto.DishDto;
import com.example.demo2.entity.Client;
import com.example.demo2.entity.Dish;
import com.example.demo2.entity.Order;
import com.example.demo2.entity.Role;
import com.example.demo2.service.ClientServiceImpl;
import com.example.demo2.service.DishServiceImpl;
import com.example.demo2.service.OrderServiceImpl;
import com.example.demo2.service.RestaurantServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    @Autowired
    DishServiceImpl dishService;
    @Autowired
    RestaurantServiceImpl restaurantService;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    ClientServiceImpl clientService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<DishDto>> getAll() {
        List<DishDto> dishes = dishService.getAll();
        if (dishes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(dishes, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getById(@PathVariable(name = "id") Long id) {
        DishDto dish = dishService.getById(id);

        if (dish != null) {
            return new ResponseEntity<>(dish, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping
    public ResponseEntity<Dish> create(@RequestBody DishDto dishDto) {
        Dish postRequest = modelMapper.map(dishDto, Dish.class);
        postRequest.setRestaurant(restaurantService.getById(dishDto.getRestaurant_id()));
        Dish dish = dishService.create(postRequest);
        return new ResponseEntity<>(postRequest, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        try {
            int result = dishService.delete(id);
            if (result == 0) {
                return new ResponseEntity<>("Not found", HttpStatus.OK);
            }
            return new ResponseEntity<>("Dish was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete dish.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/order")
    public ResponseEntity<Order> order(@PathVariable(name = "id") Long dishId) {
        DishDto dishDto = dishService.getById(dishId);
        Dish dish=modelMapper.map(dishDto, Dish.class);

        dish.setRestaurant(restaurantService.getById(dishDto.getRestaurant_id()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Client client = clientService.findByEmail(email);

        if (dish != null && client != null) {
            Order order = new Order();
            order.setClient(client);
            order.setDish(dish);
            order.setDate(LocalDate.now());

            List<Role> roles = clientService.getRoles(client.getId());
            client.setRoles(new HashSet<>(roles));
            client.setOrders(clientService.getOrders(client.getId()));
            orderService.create(order);

            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
