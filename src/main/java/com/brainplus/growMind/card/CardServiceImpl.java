package com.brainplus.growMind.card;

import com.brainplus.growMind.deck.Deck;
import com.brainplus.growMind.deck.DeckCreationRequest;
import com.brainplus.growMind.deck.DeckRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;
  private final DeckRepository deckRepository;

  @Override
  @Transactional
  public CardCreationResponse createCard(CardCreationRequest request) {
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
}
