package com.brainplus.growMind.deck;

import com.brainplus.growMind.user.AppUser;
import com.brainplus.growMind.user.UserRepository;
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

  @Override
  public DecksSearchResponse findDecksByUserId(int userId) {
    List<Deck> decks = deckRepository.findByUserId_Id(userId);

    return new DecksSearchResponse(decks);
  }

  @Override
  public DeckSearchResponse findDeckById(int deckId) {
    Deck deck = deckRepository.findById(deckId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1));

    return new DeckSearchResponse(deck);
  }

  @Override
  @Transactional
  public DeckCreationResponse createDeck(DeckCreationRequest request) {
    AppUser appUser = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new EmptyResultDataAccessException("User not found", 1));

    var deck = Deck.builder()
        .userId(appUser)
        .name(request.getName())
        .build();

    deckRepository.save(deck);

    return new DeckCreationResponse(deck);
  }

  @Override
  @Transactional
  public DeckUpdateResponse updateDeck(int deckId, DeckUpdateRequest request) {
    Deck deck = deckRepository.findById(deckId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1));

    deck.setName(request.getName());
    deckRepository.save(deck);

    return new DeckUpdateResponse(deck);
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
