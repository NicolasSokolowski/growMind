package com.brainplus.growMind.deck;

import com.brainplus.growMind.exception.ValidationException;
import com.brainplus.growMind.user.AppUser;
import com.brainplus.growMind.user.UserRepository;
import com.brainplus.growMind.validator.ObjectsValidator;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DeckServiceImpl implements DeckService {

  private final DeckRepository deckRepository;
  private final UserRepository userRepository;
  private final ObjectsValidator validator;

  @Override
  public DecksSearchResponseDto findDecksByUserId(int userId) {
    List<Deck> decks = deckRepository.findByUserId_Id(userId);

    return new DecksSearchResponseDto(decks);
  }

  @Override
  public DeckSearchResponseDto findDeckById(int deckId) {
    Deck deck = deckRepository.findById(deckId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1));

    return new DeckSearchResponseDto(deck);
  }

  @Override
  @Transactional
  public DeckCreationResponseDto createDeck(int userId, DeckCreationRequestDto request) {
    var violations = validator.validate(request);
    if (!violations.isEmpty()) {
      throw new ValidationException(violations);
    }

    AppUser appUser = userRepository.findById(userId)
        .orElseThrow(() -> new EmptyResultDataAccessException("User not found", 1));

    var deck = Deck.builder()
        .userId(appUser)
        .name(request.getName())
        .build();

    deckRepository.save(deck);

    return new DeckCreationResponseDto(deck);
  }

  @Override
  @Transactional
  public DeckUpdateResponseDto updateDeck(int deckId, DeckUpdateRequestDto request) {
    var violations = validator.validate(request);
    if (!violations.isEmpty()) {
      throw new ValidationException(violations);
    }

    Deck deck = deckRepository.findById(deckId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1));

    deck.setName(request.getName());
    deckRepository.save(deck);

    return new DeckUpdateResponseDto(deck);
  }

  @Override
  @Transactional
  public void deleteDeck(int deckId) {
    deckRepository.deleteById(deckId);
  }

  @Override
  @Transactional
  public void deleteDecks(DeckDeleteManyRequestDto request) {
    List<Integer> deckIds = request.getIds();

    for (Integer deckId : deckIds) {
      deckRepository.deleteById(deckId);
    }

  }
}
