package com.brainplus.growMind.deck;

public interface DeckService {

  DeckCreationResponse createDeck(int userId, DeckCreationRequestDto request);
  DeckUpdateResponse updateDeck(int deckId, DeckUpdateRequest request);
  void deleteDeck(int deckId);
  void deleteDecks(DeckDeleteManyRequestDto request);
  DecksSearchResponse findDecksByUserId(int userId);
  DeckSearchResponse findDeckById(int deckId);

}
