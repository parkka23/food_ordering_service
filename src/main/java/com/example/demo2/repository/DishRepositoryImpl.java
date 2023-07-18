package com.example.demo2.repository;
import com.example.demo2.dto.DishDto;
import com.example.demo2.entity.Dish;
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
public class DishRepositoryImpl {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall saveDishProcedure;
    private SimpleJdbcCall updateDishProcedure;
    private SimpleJdbcCall deleteDishProcedure;
    private SimpleJdbcCall findDishesProcedure;
    private SimpleJdbcCall findDishProcedure;

    public DishRepositoryImpl(JdbcTemplate jdbcTemplate1) {
        this.jdbcTemplate = jdbcTemplate1;
        //this.saveDishProcedure = saveDishProcedure;
    }

    @PostConstruct
    public void setup() {
        saveDishProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_save_dish");
        updateDishProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_update_dish");
        deleteDishProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_delete_dish");
        findDishProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_find_dish_by_id");
        findDishesProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_find_dishes");
    }

    public void save(Dish dish) {
        MapSqlParameterSource inParams = new MapSqlParameterSource();
        inParams.addValue("p_name", dish.getName());
        inParams.addValue("p_type", dish.getType());
        inParams.addValue("p_price", dish.getPrice());
        inParams.addValue("p_restaurant_id", dish.getRestaurant().getId());

         saveDishProcedure.executeObject(Integer.class, inParams);
    }

    public List<DishDto> findAll() {
        Map<String, Object> result = findDishesProcedure.execute();
        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("#result-set-1");

        List<DishDto> dishes = new ArrayList<>();

        for (Map<String, Object> row : resultSet) {
            DishDto dishDto = new DishDto();
            dishDto.setId((Long) row.get("id"));
            dishDto.setName((String) row.get("name"));
            dishDto.setType((String) row.get("type"));
            dishDto.setPrice((BigDecimal) row.get("price"));
            dishDto.setRestaurant_id((Long) row.get("restaurant_id"));
            dishes.add(dishDto);
        }

        return dishes;
    }

    public int deleteById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_id", id);
        try {
            deleteDishProcedure.execute(parameters);            return 1;
        }
        catch (Exception e){
            return 0;
        }

    }


    public DishDto findById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_id", id);

        Map<String, Object> result = findDishProcedure.execute(parameters);
        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("#result-set-1");

        if (resultSet.isEmpty()) {
            return null;
        } else {
            Map<String, Object> row = resultSet.get(0);
            DishDto dishDto = new DishDto();
            dishDto.setId((Long) row.get("id"));
            dishDto.setName((String) row.get("name"));
            dishDto.setType((String) row.get("type"));
            dishDto.setPrice((BigDecimal) row.get("price"));
            dishDto.setRestaurant_id((Long) row.get("restaurant_id"));

            // Set other properties as needed
            return dishDto;
        }
    }

}

