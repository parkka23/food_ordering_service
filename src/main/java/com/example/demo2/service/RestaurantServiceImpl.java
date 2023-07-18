package com.example.demo2.service;

import com.example.demo2.dto.DishDto;
import com.example.demo2.entity.Restaurant;
import com.example.demo2.repository.RestaurantRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl {
    @Autowired
    private final RestaurantRepositoryImpl restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepositoryImpl restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant create(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant;
    }
    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id);
    }

    public List<DishDto> getDishes(Long id) {
        return restaurantRepository.findDishes(id);
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant update(Long id, Restaurant updatedRestaurant) {
        Optional<Restaurant> optionalRestaurant= Optional.ofNullable(restaurantRepository.findById(id));
        if (optionalRestaurant.isPresent()) {
           restaurantRepository.update(id, updatedRestaurant);
            return restaurantRepository.findById(id);
        }
        return null;
    }

    public int delete(Long id) {

         return   restaurantRepository.deleteById(id);

    }

}
