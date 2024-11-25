package com.brainplus.growMind.deck;

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
public class DeckCreationRequestDto {

  @NotNull(message = "Field 'name' cannot be null.")
  @NotEmpty(message = "Field 'name' cannot be empty")
  @Size(min = 1, max = 50, message = "Field 'name' must be between 1 & 50 characters.")
  private String name;

}
