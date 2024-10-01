package com.boubyan.api.controller;


import com.boubyan.api.dto.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.boubyan.api.dto.LoginRequest;
import com.boubyan.api.jwt.JwtUtil;
import com.boubyan.api.dto.UserDto;
import com.boubyan.api.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        userDto.setRole("Admin"); // Set role for the user
        userService.saveUser(userDto); // Save user with hashed password

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Load user details and generate JWT token
        UserPrincipal userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        String role = userDetails.getAuthorities().iterator().next().toString();
        String token = jwtUtil.generateToken(userDetails.getUsername(), role, userDetails.getId());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String header) {
        if (header != null && header.startsWith("Bearer ")) {
            String jwt = header.substring(7);
            String username = jwtUtil.extractUsername(jwt);

            // Validate the old token
            if (jwtUtil.validateToken(jwt, username)) {
                String role = jwtUtil.extractRole(jwt);
                long userId = jwtUtil.extractUserId(jwt);
                // Generate a new token
                String newToken = jwtUtil.generateToken(username, role, userId);
                return ResponseEntity.ok(newToken);
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired token");
            }
        }

        return ResponseEntity.badRequest().body("Token is missing");
    }
}

