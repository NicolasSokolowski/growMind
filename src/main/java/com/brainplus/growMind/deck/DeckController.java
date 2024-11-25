package com.brainplus.growMind.deck;

import com.brainplus.growMind.config.JwtService;
import com.brainplus.growMind.user.AppUser;
import com.brainplus.growMind.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class DeckController {

  private final DeckService deckService;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final DeckRepository deckRepository;

  @GetMapping
  public ResponseEntity<DecksSearchResponseDto> getAllDecksByUserId(
      @RequestParam(name = "userId") int userId,
      @RequestHeader("Authorization") String token
  ) throws AccessDeniedException {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    AppUser requestedUser = userRepository.findById(userId)
        .orElseThrow(() -> new EmptyResultDataAccessException("User not found", 1));

    if (authenticatedUserId != requestedUser.getId()) {
      throw new AccessDeniedException("You are not authorized to get a deck for this user.");
    }

    return ResponseEntity.ok(deckService.findDecksByUserId(userId));
  }

  @GetMapping("/{deckId}")
  public ResponseEntity<DeckSearchResponseDto> getDeckById(
      @PathVariable int deckId,
      @RequestHeader("Authorization") String token
  ) throws AccessDeniedException {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    Deck deck = deckRepository.findById(deckId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1));

    AppUser requestedUser = userRepository.findById(deck.getUserId().getId())
        .orElseThrow(() -> new EmptyResultDataAccessException("User not found", 1));

    if (authenticatedUserId != requestedUser.getId()) {
      throw new AccessDeniedException("You are not authorized to get a deck for this user.");
    }

    return ResponseEntity.ok(deckService.findDeckById(deckId));
  }

  @PostMapping
  public ResponseEntity<DeckCreationResponseDto> createDeck(
      @RequestBody DeckCreationRequestDto request,
      @RequestHeader("Authorization") String token
  ) {
    int userId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    return ResponseEntity.ok(deckService.createDeck(userId, request));
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteDecks(
      @RequestBody DeckDeleteManyRequestDto request,
      @RequestHeader("Authorization") String token
  ) throws AccessDeniedException {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    List<Integer> deckIds = request.getIds();

    for (Integer deckId : deckIds) {
      Deck deck = deckRepository.findById(deckId)
          .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1));

      AppUser appUser = deck.getUserId();

      if (appUser.getId() != authenticatedUserId) {
        throw new AccessDeniedException("You are not authorized to update a deck for this user.");
      }
    }

    deckService.deleteDecks(request);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PutMapping("/{deckId}")
  public ResponseEntity<DeckUpdateResponseDto> updateDeck(
      @RequestBody DeckUpdateRequestDto request,
      @PathVariable int deckId,
      @RequestHeader("Authorization") String token
  ) throws AccessDeniedException {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    Deck foundDeck = deckRepository.findById(deckId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1));

    if (foundDeck.getUserId().getId() != authenticatedUserId) {
      throw new AccessDeniedException("You are not authorized to update a deck for this user.");
    }

    return ResponseEntity.ok(deckService.updateDeck(deckId, request));
  }

  @DeleteMapping("/{deckId}")
  public ResponseEntity<Void> deleteDeck(
      @PathVariable int deckId,
      @RequestHeader("Authorization") String token
  ) throws AccessDeniedException {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    Deck foundDeck = deckRepository.findById(deckId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1));

    if (foundDeck.getUserId().getId() != authenticatedUserId) {
      throw new AccessDeniedException("You are not authorized to delete a deck for this user.");
    }

    deckService.deleteDeck(deckId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
