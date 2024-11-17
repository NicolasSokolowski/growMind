package com.brainplus.growMind.deck;

import com.brainplus.growMind.config.JwtService;
import com.brainplus.growMind.user.AppUser;
import com.brainplus.growMind.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class DeckController {

  private final DeckService deckService;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final DeckRepository deckRepository;

  @GetMapping
  public ResponseEntity<DecksSearchResponse> getAllDecksByUserId(
      @RequestParam(name = "userId") int userId,
      @RequestHeader("Authorization") String token
  ) {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    AppUser requestedUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (authenticatedUserId != requestedUser.getId()) {
      throw new RuntimeException("Not Authorized.");
    }

    return ResponseEntity.ok(deckService.findDecksByUserId(userId));
  }

  @GetMapping("/{deckId}")
  public ResponseEntity<DeckSearchResponse> getDeckById(
      @PathVariable int deckId,
      @RequestHeader("Authorization") String token
  ) {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    Deck deck = deckRepository.findById(deckId)
        .orElseThrow(() -> new RuntimeException("Deck not found"));

    AppUser requestedUser = userRepository.findById(deck.getUserId().getId())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (authenticatedUserId != requestedUser.getId()) {
      throw new RuntimeException("Not Authorized.");
    }

    return ResponseEntity.ok(deckService.findDeckById(deckId));
  }

  @PostMapping
  public ResponseEntity<DeckCreationResponse> createDeck(
      @RequestBody DeckCreationRequest request,
      @RequestHeader("Authorization") String token
  ) {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    AppUser requestedUser = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (authenticatedUserId != requestedUser.getId()) {
      throw new RuntimeException("Not Authorized.");
    }

    return ResponseEntity.ok(deckService.createDeck(request));
  }

  @PutMapping("/{deckId}")
  public ResponseEntity<DeckUpdateResponse> updateDeck(
      @RequestBody DeckUpdateRequest request,
      @PathVariable int deckId,
      @RequestHeader("Authorization") String token
  ) {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    Optional<Deck> foundDeck = deckRepository.findById(deckId);

    if (foundDeck.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    Deck deck = foundDeck.get();

    if (deck.getUserId().getId() != authenticatedUserId) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(deckService.updateDeck(deckId, request));
  }

  @DeleteMapping("/{deckId}")
  public ResponseEntity<Void> deleteDeck(
      @PathVariable int deckId,
      @RequestHeader("Authorization") String token
  ) {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    Optional<Deck> foundDeck = deckRepository.findById(deckId);

    if (foundDeck.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    Deck deck = foundDeck.get();

    if (deck.getUserId().getId() != authenticatedUserId) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    deckService.deleteDeck(deckId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
