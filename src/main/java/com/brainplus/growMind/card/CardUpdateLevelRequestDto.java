package com.brainplus.growMind.card;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardUpdateLevelRequestDto {

  @NotNull
  private Integer id;

  @NotNull
  private Integer difficulty;

}
