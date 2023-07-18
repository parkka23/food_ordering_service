package com.example.demo2.repository;

import com.example.demo2.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl  {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public int save(Role role) {
        return jdbcTemplate.update("INSERT INTO roles (name) VALUES (?)", new Object[] {role.getName()});
    }

public Optional<Role> findByName(String name) {
    List<Role> roles = jdbcTemplate.query(
            "SELECT * FROM roles WHERE name=?",
            BeanPropertyRowMapper.newInstance(Role.class),
            name
    );

    if (roles.isEmpty()) {
        return Optional.empty(); // Return empty Optional if no result is found
    } else {
        return Optional.of(roles.get(0)); // Return the first role in the list wrapped in Optional
    }
}
}
