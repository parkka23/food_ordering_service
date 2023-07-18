package com.example.demo2.controller;

import com.example.demo2.dto.LoginDto;
import com.example.demo2.dto.RoleDto;
import com.example.demo2.entity.Client;
import com.example.demo2.entity.Role;
import com.example.demo2.service.ClientServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
public class ClientController {
    @Autowired
    private final ClientServiceImpl clientService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ClientController(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> create(@RequestBody Client client) {
        Client existingClient = clientService.findByEmail(client.getEmail());

        if (existingClient != null) {
            return new ResponseEntity<String>("Already exists", HttpStatus.NOT_ACCEPTABLE);
        } else {
            clientService.create(client);
            return new ResponseEntity<>("Registered successfully", HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser() {
        return new ResponseEntity<>("User logged-in successfully!", HttpStatus.OK);
    }

    @RequestMapping(value = "/logout_success")
    public ResponseEntity<String> logoutUser() {
        return new ResponseEntity<>("User logged out successfully.", HttpStatus.OK);
    }

    @RequestMapping(value = "/logout_fail")
    public ResponseEntity<String> logoutFail() {
        return new ResponseEntity<>("Not authenticated to logout.", HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/details")
    public ResponseEntity<Client> getDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Client client = clientService.findByEmail(email);

        if (client != null) {
            List<Role> roles = clientService.getRoles(client.getId());
            client.setRoles(new HashSet<>(roles));
            client.setOrders(clientService.getOrders(client.getId()));
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}