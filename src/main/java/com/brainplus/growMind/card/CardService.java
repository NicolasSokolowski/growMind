package com.brainplus.growMind.card;

public interface CardService {

  CardsGetByUserIdResponseDto getAllCardsByUserId(int userId);
  CardCreationResponseDto createCardAndAddToDecks(CardCreationRequestDto request);
  CardUpdateResponseDto updateCard(int cardId, CardUpdateRequestDto request);
  CardsUpdateResponseDto updateCardsLevel(CardsUpdateManyRequestDto request);
  void deleteCard(int cardId);
  void deleteCards(CardDeleteManyRequestDto request);

}
