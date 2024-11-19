package com.brainplus.growMind.card;

import com.brainplus.growMind.deck.Deck;
import com.brainplus.growMind.deck.DeckRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;
  private final DeckRepository deckRepository;

  @Override
  @Transactional
  public CardCreationResponse createCardAndAddToDecks(CardCreationRequest request) {
    List<Card> createdCards = new ArrayList<>();

    List<Integer> deckIds = request.getDeckIds();
    for (Integer deckId : deckIds) {
      Deck deck = deckRepository.findById(deckId)
          .orElseThrow(() -> new RuntimeException("Deck not found"));

      Card card = Card.builder()
          .frontSide(request.getFrontSide())
          .backSide((request.getBackSide()))
          .deck(deck)
          .build();

      cardRepository.save(card);
      createdCards.add(card);

    }

    return new CardCreationResponse(createdCards);
  }

  @Override
  @Transactional
  public CardUpdateResponse updateCard(int cardId, CardUpdateRequest request) {
    Card card = cardRepository.findById(cardId)
        .orElseThrow(() -> new RuntimeException("Card not found"));

    card.setFrontSide(request.getFrontSide());
    card.setBackSide(request.getBackSide());
    cardRepository.save(card);

    return new CardUpdateResponse(card);
  }

  @Override
  @Transactional
  public void deleteCard(int cardId) {
    cardRepository.deleteById(cardId);
  }
}
