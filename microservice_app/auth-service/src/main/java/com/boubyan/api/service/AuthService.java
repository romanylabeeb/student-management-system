package com.boubyan.api.service;

import com.boubyan.api.dto.AuthResponse;
import com.boubyan.api.dto.LoginRequest;
import com.boubyan.api.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    public ResponseEntity<AuthResponse>  authenticate(LoginRequest loginRequest);

    public void register(UserDto userDto);

    public ResponseEntity<?> refreshToken(String token);

}
