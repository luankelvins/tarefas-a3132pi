package com.tarefas.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("API online");
    }
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("API OK");
}
}
