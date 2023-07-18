package com.example.demo2.mapper;

import com.example.demo2.dto.ClientDto;
import com.example.demo2.entity.Client;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<ClientDto> {
    @Override
    public ClientDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ClientDto(rs.getString("username"), rs.getString("email"),rs.getString("password"), rs.getString("role"));
    }
}