package com.example.personalfinancetracker.controller;

import com.example.personalfinancetracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(authService.login(username,password));
    }
}
