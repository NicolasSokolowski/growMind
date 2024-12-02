package com.brainplus.growMind.user;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequestDto {

  @NotNull(message = "Field 'name' cannot be null.")
  @NotEmpty(message = "Field 'name' cannot be empty")
  @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
  private String currentPassword;

  @NotNull(message = "Field 'name' cannot be null.")
  @NotEmpty(message = "Field 'name' cannot be empty")
  @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
  private String newPassword;

  @NotNull(message = "Field 'name' cannot be null.")
  @NotEmpty(message = "Field 'name' cannot be empty")
  @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters.")
  private String confirmationPassword;

}
