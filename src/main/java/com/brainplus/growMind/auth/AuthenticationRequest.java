package com.brainplus.growMind.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  @Email
  @NotNull(message = "Field 'name' cannot be null.")
  @NotEmpty(message = "Field 'name' cannot be empty")
  private String email;

  @NotNull(message = "Field 'name' cannot be null.")
  @NotEmpty(message = "Field 'name' cannot be empty")
  @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
  String password;
}
