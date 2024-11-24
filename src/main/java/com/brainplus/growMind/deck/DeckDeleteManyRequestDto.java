package com.brainplus.growMind.deck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeckDeleteManyRequestDto {

  private List<Integer> ids;
}
