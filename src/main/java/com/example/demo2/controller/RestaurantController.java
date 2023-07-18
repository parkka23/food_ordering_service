package com.example.demo2.controller;

import com.example.demo2.dto.RestaurantDto;
import com.example.demo2.entity.Restaurant;
import com.example.demo2.service.RestaurantServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantServiceImpl restaurantService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getAll() {
        List<RestaurantDto> restaurants = restaurantService.getAll().stream().map(post -> modelMapper.map(post, RestaurantDto.class)).collect(Collectors.toList());
        if (restaurants.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(restaurants, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable(name = "id") Long id) {
        Restaurant restaurant = restaurantService.getById(id);

        if (restaurant != null) {
            restaurant.setDishes(restaurantService.getDishes(id));
            return new ResponseEntity<>(restaurant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping
    public ResponseEntity<RestaurantDto> create(@RequestBody RestaurantDto restaurantDto) {
        Restaurant postRequest = modelMapper.map(restaurantDto, Restaurant.class);

        Restaurant restaurant = restaurantService.create(postRequest);

        RestaurantDto postResponse = modelMapper.map(restaurant, RestaurantDto.class);

        return new ResponseEntity<RestaurantDto>(postResponse, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody RestaurantDto restaurantDto) {
        Restaurant restaurant=restaurantService.getById(id);
        if (restaurant!=null) {
            Restaurant postRequest = modelMapper.map(restaurantDto, Restaurant.class);

            restaurantService.update(id, postRequest);

            return new ResponseEntity<>("Restaurant was updated successfully.", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Cannot find restaurant with id=" + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        try {
            int result = restaurantService.delete(id);
            if (result == 0) {
                return new ResponseEntity<>("Not found", HttpStatus.OK);
            }
            return new ResponseEntity<>("Restaurant was deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cannot delete restaurant.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}