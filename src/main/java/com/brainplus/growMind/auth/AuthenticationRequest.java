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
  @Min(value = 8, message = "Password must be 8 characters min.")
  @Max(value = 128, message = "Password must be 128 characters max.")
  String password;
}
