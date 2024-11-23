package com.brainplus.growMind.card;

import com.brainplus.growMind.deck.Deck;
import com.brainplus.growMind.deck.DeckRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
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
  public GetCardsByUserIdResponse getAllCardsByUserId(int userId) {
    List<Card> cards = new ArrayList<>();
    List<Deck> decks = deckRepository.findByUserId_Id(userId);

    for (Deck deck : decks) {
      List<Card> foundCards = deck.getCards();
      cards.addAll(foundCards);
    }

    return new GetCardsByUserIdResponse(cards);
  }

  @Override
  @Transactional
  public CardCreationResponse createCardAndAddToDecks(CardCreationRequest request) {
    List<Card> createdCards = new ArrayList<>();

    List<Integer> deckIds = request.getDeckIds();
    for (Integer deckId : deckIds) {
      Deck deck = deckRepository.findById(deckId)
          .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1));

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
        .orElseThrow(() -> new EmptyResultDataAccessException("Card not found", 1));

    card.setFrontSide(request.getFrontSide());
    card.setBackSide(request.getBackSide());
    cardRepository.save(card);

    return new CardUpdateResponse(card);
  }

  @Override
  @Transactional
  public CardsResponseDto updateCards(UpdateManyCardsRequestDto request) {
    List<Card> cardsToUpdate = new ArrayList<>();

    for (Card card : request.getCards()) {
      Card cardToUpdate = cardRepository.findById(card.getId())
          .orElseThrow(() -> new EmptyResultDataAccessException("Card not found", 1));

      cardToUpdate.setFrontSide(card.getFrontSide());
      cardToUpdate.setBackSide(card.getBackSide());
      cardToUpdate.setDifficulty(card.getDifficulty());

      cardsToUpdate.add(cardToUpdate);

    }

    cardRepository.saveAll(cardsToUpdate);

    return new CardsResponseDto(cardsToUpdate);
  }

  @Override
  @Transactional
  public void deleteCard(int cardId) {
    cardRepository.deleteById(cardId);
  }

  @Override
  @Transactional
  public void deleteCards(CardDeleteManyRequestDto request) {
    List<Integer> cardIds = request.getIds();

    for (Integer cardId : cardIds) {
      cardRepository.deleteById(cardId);
    }
  }
}
