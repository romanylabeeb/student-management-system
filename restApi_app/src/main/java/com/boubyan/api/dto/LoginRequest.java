package com.boubyan.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
   @NotNull(message = "Username is required")
   private String username;
   @NotNull(message = "Password is required")
   private String password;

}
