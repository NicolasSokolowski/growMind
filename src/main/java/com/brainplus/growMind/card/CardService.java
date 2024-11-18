package com.brainplus.growMind.card;

public interface CardService {

  CardCreationResponse createCardAndAddToDecks(CardCreationRequest request);
}
