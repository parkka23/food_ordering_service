package com.example.demo2.repository;

import com.example.demo2.dto.OrderDto;
import com.example.demo2.entity.Client;
import com.example.demo2.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ClientRepositoryImpl {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall saveClientProcedure;
    private SimpleJdbcCall findClientByEmailProcedure;
    private SimpleJdbcCall findOrdersProcedure;
    private SimpleJdbcCall findRolesProcedure;


    public ClientRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void setup() {
        saveClientProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_save_client");
        findClientByEmailProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_find_client_by_email")
                .returningResultSet("client", BeanPropertyRowMapper.newInstance(Client.class));
        findOrdersProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_find_orders_by_client_id");
        findRolesProcedure = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_find_roles_by_client_id");

    }

    public void save(Client client) {
        saveClientProcedure.execute(client.getUsername(), client.getEmail(), client.getPassword());
    }

    public Client findByEmail(String email) {
        try {
            Map<String, Object> result = findClientByEmailProcedure.execute(email);
            List<Client> clients = (List<Client>) result.get("client");
            return clients != null && !clients.isEmpty() ? clients.get(0) : null;
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }


    public List<OrderDto> findOrders(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_client_id", id);

        Map<String, Object> result = findOrdersProcedure.execute(parameters);
        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("#result-set-1");

        List<OrderDto> orders = new ArrayList<>();

        for (Map<String, Object> row : resultSet) {
            OrderDto order = new OrderDto();
            order.setId((Long) row.get("id"));
            order.setClient_id((Long) row.get("client_id"));
            order.setDish_id((Long) row.get("dish_id"));

            java.sql.Date dateSql = (java.sql.Date) row.get("date");
            order.setDate(dateSql.toLocalDate());

            orders.add(order);
        }
        return orders;
    }

    public List<Role> findRoles(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("p_client_id", id);

        Map<String, Object> result = findRolesProcedure.execute(parameters);
        List<Map<String, Object>> resultSet = (List<Map<String, Object>>) result.get("#result-set-1");

        List<Role> roles = new ArrayList<>();

        for (Map<String, Object> row : resultSet) {
            Role role = new Role();
            role.setId((Long) row.get("id"));
            role.setName((String) row.get("name"));

            roles.add(role);
        }
        return roles;
    }

}
