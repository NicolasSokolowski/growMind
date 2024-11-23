package com.brainplus.growMind.card;

public interface CardService {

  GetCardsByUserIdResponse getAllCardsByUserId(int userId);
  CardCreationResponse createCardAndAddToDecks(CardCreationRequest request);
  CardUpdateResponse updateCard(int cardId, CardUpdateRequest request);
  CardsResponseDto updateCards(UpdateManyCardsRequestDto request);
  void deleteCard(int cardId);
  void deleteCards(CardDeleteManyRequestDto request);

}
