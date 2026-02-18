package com.example.storeapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String home() {
        return "Store API is running 🚀";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }

    @GetMapping("/version")
    public String version() {
        return "v1.0.0";
    }
}
