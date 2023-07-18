package com.example.demo2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private Long id;
    private String name;
    //private List<Client> clients = new ArrayList<>();
    public Role(String name) {
        this.name = name;
    }
}