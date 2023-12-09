package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
  @NotBlank(message = "username not blank")
  private String username;
  @NotBlank(message = "password not blank")
  private String password;
}
