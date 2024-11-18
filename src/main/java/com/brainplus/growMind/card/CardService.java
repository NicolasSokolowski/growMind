package com.brainplus.growMind.card;

import com.brainplus.growMind.deck.DeckCreationRequest;

public interface CardService {

  CardCreationResponse createCard(DeckCreationRequest request);
}
