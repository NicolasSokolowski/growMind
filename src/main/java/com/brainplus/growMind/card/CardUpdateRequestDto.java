package com.brainplus.growMind.card;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardUpdateRequestDto {

  @NotNull(message = "Field 'backSide' cannot be null.")
  @NotEmpty(message = "Field 'backSide' cannot be empty")
  @Size(min = 1, max = 50, message = "Field 'backSide' must be between 1 & 50 characters.")
  private String frontSide;

  @NotNull(message = "Field 'backSide' cannot be null.")
  @NotEmpty(message = "Field 'backSide' cannot be empty")
  @Size(min = 1, max = 50, message = "Field 'backSide' must be between 1 & 50 characters.")
  private String backSide;

}
