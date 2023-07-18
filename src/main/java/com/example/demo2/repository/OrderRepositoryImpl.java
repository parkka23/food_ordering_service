package com.example.demo2.repository;

import com.example.demo2.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class OrderRepositoryImpl{
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall saveOrderProcedure;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    @PostConstruct
    public void setup() {
        saveOrderProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_save_order");
    }
    public void save(Order order) {
        MapSqlParameterSource inParams = new MapSqlParameterSource();
        inParams.addValue("p_client_id", order.getClient().getId());
        inParams.addValue("p_dish_id", order.getDish().getId());
        inParams.addValue("p_date", order.getDate());

        saveOrderProcedure.executeObject(Integer.class, inParams);
    }
}
