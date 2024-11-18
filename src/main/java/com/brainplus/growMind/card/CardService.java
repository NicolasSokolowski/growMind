package com.brainplus.growMind.card;

public interface CardService {

  CardCreationResponse createCardAndAddToDecks(CardCreationRequest request);
  CardUpdateResponse updateCard(int cardId, CardUpdateRequest request);
  void deleteCard(int cardId);

}
