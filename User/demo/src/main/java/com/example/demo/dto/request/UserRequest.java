package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
  @NotBlank(message = "username not blank")
  private String username;
  @NotBlank(message = " password not blank")
  private String password;
  @NotBlank(message = "email not blank")
  private String email;
}
