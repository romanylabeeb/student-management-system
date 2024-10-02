package com.boubyan.api.dto;

import com.boubyan.api.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {

    @NotNull(message = "username is required")
    private String username;

    @NotNull(message = "password is required")
    private String password;

    private String role;

    public User buildUser(){
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        user.setRole(role);
        return user;
    }
}
