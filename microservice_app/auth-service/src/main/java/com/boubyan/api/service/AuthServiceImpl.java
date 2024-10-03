package com.boubyan.api.service;

import com.boubyan.api.dto.AuthResponse;
import com.boubyan.api.dto.LoginRequest;
import com.boubyan.api.dto.UserDto;
import com.boubyan.api.dto.UserPrincipal;
import com.boubyan.api.exception.ErrorResponse;
import com.boubyan.api.jwt.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<AuthResponse> authenticate(LoginRequest loginRequest) {
        // authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // get user details
        UserPrincipal userDetails = userService.loadUserByUsername(loginRequest.getUsername());
        String role = userDetails.getAuthorities().iterator().next().toString();
        // generate JWT token
        String token = jwtUtil.generateToken(userDetails.getUsername(), role, userDetails.getId());

        AuthResponse res = new AuthResponse(userDetails.getUsername(), role, token);
        return ResponseEntity.ok(res);
    }

    @Override
    public void register(UserDto userDto) {
        userDto.setRole("Admin");
        userService.saveUser(userDto);

    }

    @Override
    public ResponseEntity<?> refreshToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            String jwt = header.substring(7);
            String username = jwtUtil.extractUsername(jwt);

            // validate the old token
            if (jwtUtil.validateToken(jwt)) {
                String role = jwtUtil.extractRole(jwt);
                long userId = jwtUtil.extractUserId(jwt);
                // generate new token
                String newToken = jwtUtil.generateToken(username, role, userId);
                AuthResponse res = new AuthResponse(username, role, newToken);
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Invalid or expired token", ""));
            }
        }

        // token is missing
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Token is missing", ""));
    }
}
