package com.brainplus.growMind.deck;

import com.brainplus.growMind.user.AppUser;
import com.brainplus.growMind.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeckServiceImpl implements DeckService {

  private final DeckRepository deckRepository;
  private final UserRepository userRepository;

  public DeckServiceImpl(DeckRepository deckRepository, UserRepository userRepository) {
    this.deckRepository = deckRepository;
    this.userRepository = userRepository;
  }

  @Override
  public DecksSearchResponse findDecksByUserId(int userId) {
    List<Deck> decks = deckRepository.findByUserId_Id(userId);

    return new DecksSearchResponse(decks);
  }

  @Override
  public DeckSearchResponse findDeckById(int deckId) {
    Deck deck = deckRepository.findById(deckId)
        .orElseThrow(() -> new RuntimeException("Deck not found"));



    return new DeckSearchResponse(deck);
  }

  @Override
  @Transactional
  public DeckCreationResponse createDeck(DeckCreationRequest request) {
    AppUser appUser = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found."));

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
        .orElseThrow(() -> new RuntimeException("Deck not found"));

    deck.setName(request.getName());
    deckRepository.save(deck);

    return new DeckUpdateResponse(deck);
  }

  @Override
  @Transactional
  public void deleteDeck(int deckId) {
    deckRepository.deleteById(deckId);
  }
}
