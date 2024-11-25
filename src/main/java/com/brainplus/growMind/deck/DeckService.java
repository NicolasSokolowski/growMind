package com.brainplus.growMind.deck;

public interface DeckService {

  DeckCreationResponseDto createDeck(int userId, DeckCreationRequestDto request);
  DeckUpdateResponseDto updateDeck(int deckId, DeckUpdateRequestDto request);
  void deleteDeck(int deckId);
  void deleteDecks(DeckDeleteManyRequestDto request);
  DecksSearchResponseDto findDecksByUserId(int userId);
  DeckSearchResponseDto findDeckById(int deckId);

}
