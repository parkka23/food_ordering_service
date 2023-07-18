package com.example.demo2.service;

import com.example.demo2.dto.OrderDto;
import com.example.demo2.entity.Client;
import com.example.demo2.entity.Role;
import com.example.demo2.repository.ClientRepositoryImpl;
import com.example.demo2.repository.RoleRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ClientServiceImpl {
    @Autowired
    ClientRepositoryImpl clientRepository;

    @Autowired
    RoleRepositoryImpl roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepositoryImpl clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client create(Client client) {
        if (client.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            roleRepository.findByName("ROLE_USER").ifPresent(roles::add);
            client.setRoles(roles);
        }

        client.setPassword(passwordEncoder.encode(client.getPassword()));
        clientRepository.save(client);
        return client;
    }

    public List<OrderDto> getOrders(Long id) {
        return clientRepository.findOrders(id);
    }

    public List<Role> getRoles(Long id) {
        return clientRepository.findRoles(id);
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

}
