package com.brainplus.growMind.card;

import com.brainplus.growMind.deck.Deck;
import com.brainplus.growMind.deck.DeckRepository;
import com.brainplus.growMind.exception.ValidationException;
import com.brainplus.growMind.validator.ObjectsValidator;
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
  private final ObjectsValidator validator;

  @Override
  public CardsGetByUserIdResponseDto getAllCardsByUserId(int userId) {
    List<Card> cards = new ArrayList<>();
    List<Deck> decks = deckRepository.findByUserId_Id(userId);

    for (Deck deck : decks) {
      List<Card> foundCards = deck.getCards();
      cards.addAll(foundCards);
    }

    return new CardsGetByUserIdResponseDto(cards);
  }

  @Override
  @Transactional
  public CardCreationResponseDto createCardAndAddToDecks(CardCreationRequestDto request) {
    validator.validate(request);

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

    return new CardCreationResponseDto(createdCards);
  }

  @Override
  @Transactional
  public CardUpdateResponseDto updateCard(int cardId, CardUpdateRequestDto request) {
    validator.validate(request);

    Card card = cardRepository.findById(cardId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Card not found", 1));

    card.setFrontSide(request.getFrontSide());
    card.setBackSide(request.getBackSide());
    cardRepository.save(card);

    return new CardUpdateResponseDto(card);
  }

  @Override
  @Transactional
  public CardsUpdateResponseDto updateCardsLevel(CardsUpdateManyRequestDto request) {
    validator.validate(request);

    List<Card> cardsToUpdate = new ArrayList<>();

    for (CardUpdateLevelRequestDto card : request.getCards()) {
      Card cardToUpdate = cardRepository.findById(card.getId())
          .orElseThrow(() -> new EmptyResultDataAccessException("Card not found", 1));

      cardToUpdate.setDifficulty(card.getDifficulty());

      cardsToUpdate.add(cardToUpdate);

    }

    cardRepository.saveAll(cardsToUpdate);

    return new CardsUpdateResponseDto(cardsToUpdate);
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
