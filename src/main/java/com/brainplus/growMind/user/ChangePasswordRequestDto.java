package com.brainplus.growMind.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequestDto {

  @NotNull(message = "Field 'name' cannot be null.")
  @NotEmpty(message = "Field 'name' cannot be empty")
  @Min(value = 8, message = "Password must be 8 characters min.")
  @Max(value = 128, message = "Password must be 128 characters max.")
  private String currentPassword;

  @NotNull(message = "Field 'name' cannot be null.")
  @NotEmpty(message = "Field 'name' cannot be empty")
  @Min(value = 8, message = "Password must be 8 characters min.")
  @Max(value = 128, message = "Password must be 128 characters max.")
  private String newPassword;

  @NotNull(message = "Field 'name' cannot be null.")
  @NotEmpty(message = "Field 'name' cannot be empty")
  @Min(value = 8, message = "Password must be 8 characters min.")
  @Max(value = 128, message = "Password must be 128 characters max.")
  private String confirmationPassword;

}
