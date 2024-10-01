package com.boubyan.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
   @NotNull(message = "Email is required")
   private String email;
   @NotNull(message = "Password is required")
   private String password;

}
