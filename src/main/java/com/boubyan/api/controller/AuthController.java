package com.boubyan.api.controller;


import com.boubyan.api.dto.AuthResponse;
import com.boubyan.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.boubyan.api.dto.LoginRequest;
import com.boubyan.api.dto.UserDto;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
      try {
        authService.register(userDto); // Save user with hashed password
      return  ResponseEntity.status(HttpStatus.CREATED).body("Successfully created user");
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body("An error occurred during registration.");
      }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String header) {
        return authService.refreshToken(header);
    }
}

