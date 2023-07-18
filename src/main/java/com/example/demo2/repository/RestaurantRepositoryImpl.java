package com.example.demo2.repository;

import com.example.demo2.dto.DishDto;
import com.example.demo2.entity.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class RestaurantRepositoryImpl {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall saveRestaurantProcedure;
    private SimpleJdbcCall updateRestaurantProcedure;
    private SimpleJdbcCall findRestaurantsProcedure;
    private SimpleJdbcCall deleteRestaurantProcedure;
    private SimpleJdbcCall findRestaurantProcedure;
    private SimpleJdbcCall findDishesProcedure;


    public RestaurantRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    @PostConstruct
    public void setup() {
        saveRestaurantProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_save_restaurant");
        updateRestaurantProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_update_restaurant");
        findRestaurantsProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_find_restaurants");
        deleteRestaurantProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_delete_restaurant");
        findRestaurantProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_find_restaurant_by_id");
        findDishesProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_find_dishes_by_restaurant_id");
    }

    public void save(Restaurant restaurant) {
        MapSqlParameterSource inParams = new MapSqlParameterSource();
        inParams.addValue("p_name", restaurant.getName());
        inParams.addValue("p_description", restaurant.getDescription());
        saveRestaurantProcedure.executeObject(Integer.class, inParams);
    }


    public void update(Long id, Restaurant restaurant) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_id", id)
                .addValue("p_name", restaurant.getName())
                .addValue("p_description", restaurant.getDescription());

        updateRestaurantProcedure.execute(parameters);
    }

    public Restaurant findById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_id", id);

        Map<String, Object> result = findRestaurantProcedure.execute(parameters);
        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("#result-set-1");

        if (resultSet.isEmpty()) {
            return null;
        } else {
            Map<String, Object> row = resultSet.get(0);
            Restaurant restaurant = new Restaurant();
            restaurant.setId((Long) row.get("id"));
            restaurant.setName((String) row.get("name"));
            restaurant.setDescription((String) row.get("description"));
            return restaurant;
        }
    }
    public List<DishDto> findDishes(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_restaurant_id", id);

        Map<String, Object> result = findDishesProcedure.execute(parameters);
        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("#result-set-1");

        List<DishDto> dishes = new ArrayList<>();

        for (Map<String, Object> row : resultSet) {
            DishDto dish = new DishDto();
            dish.setId((Long) row.get("id"));
            dish.setName((String) row.get("name"));
            dish.setType((String) row.get("type"));
            dish.setPrice((BigDecimal) row.get("price"));
            dish.setRestaurant_id((Long) row.get("restaurant_id"));
            dishes.add(dish);
        }

        return dishes;
    }

    public List<Restaurant> findAll() {
        Map<String, Object> result = findRestaurantsProcedure.execute();
        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("#result-set-1");

        List<Restaurant> restaurants = new ArrayList<>();

        for (Map<String, Object> row : resultSet) {
            Restaurant restaurant = new Restaurant();
            restaurant.setId((Long) row.get("id"));
            restaurant.setName((String) row.get("name"));
            restaurant.setDescription((String) row.get("description"));
            restaurants.add(restaurant);
        }

        return restaurants;
    }

    public int deleteById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_id", id);
        try {
            deleteRestaurantProcedure.execute(parameters);
            return 1;
        }
          catch (Exception e){
            return 0;
            }
    }

}
