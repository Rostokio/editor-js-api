package com.example.demo.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity<Object> getData() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(new ClassPathResource("hardcode-data.json"));
    }
}
