package com.example.demo2.entity;

import com.example.demo2.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private List<OrderDto> orders;
}
