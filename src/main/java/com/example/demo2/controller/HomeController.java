package com.example.demo2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        String hello="HELLO";
        return ResponseEntity.ok(hello);
    }
}