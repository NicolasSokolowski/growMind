package com.brainplus.growMind.card;

import com.brainplus.growMind.deck.Deck;
import com.brainplus.growMind.deck.DeckCreationRequest;
import com.brainplus.growMind.deck.DeckRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;
  private final DeckRepository deckRepository;

  public CardServiceImpl(CardRepository cardRepository, DeckRepository deckRepository) {
    this.cardRepository = cardRepository;
    this.deckRepository = deckRepository;
  }

  @Override
  @Transactional
  public CardCreationResponse createCardAndAddToDecks(CardCreationRequest request) {
    Card card = Card.builder()
        .frontSide(request.getFrontSide())
        .backSide((request.getBackSide()))
        .build();

    cardRepository.save(card);

    List<Integer> deckIds = request.getDeckIds();
    for (Integer deckId : deckIds) {
      Deck deck = deckRepository.findById(deckId)
          .orElseThrow(() -> new RuntimeException("Deck not found"));
      deck.getCards().add(card);
      deckRepository.save(deck);
    }

    return new CardCreationResponse(card);
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
