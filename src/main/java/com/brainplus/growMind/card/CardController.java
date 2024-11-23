package com.brainplus.growMind.card;

import com.brainplus.growMind.config.JwtService;
import com.brainplus.growMind.deck.DeckRepository;
import com.brainplus.growMind.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

  private final CardService cardService;
  private final DeckRepository deckRepository;
  private final CardRepository cardRepository;
  private final JwtService jwtService;

  @GetMapping
  public ResponseEntity<GetCardsByUserIdResponse> getCardsByUserId(
      @RequestHeader("Authorization") String token
  ) {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    return ResponseEntity.ok(cardService.getAllCardsByUserId(authenticatedUserId));
  }

  @PostMapping
  public ResponseEntity<CardCreationResponse> createCardAndAddToDecks(
      @RequestBody CardCreationRequest request,
      @RequestHeader("Authorization") String token
  ) throws AccessDeniedException {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));
    List<Integer> deckIds = request.getDeckIds();

    for (Integer deckId : deckIds) {
      AppUser appUser = deckRepository.findById(deckId)
          .orElseThrow(() -> new EmptyResultDataAccessException("Deck not found", 1)).getUserId();

      if (authenticatedUserId != appUser.getId()) {
        throw new AccessDeniedException("You are not authorized to create cards for this user.");
      }
    }

    return ResponseEntity.ok(cardService.createCardAndAddToDecks(request));
  }

  @PutMapping
  public ResponseEntity<CardsResponseDto> updateManyCards(
      @RequestBody UpdateManyCardsRequestDto request,
      @RequestHeader("Authorization") String token
  ) throws AccessDeniedException {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    for (Card card : request.getCards()) {
      Integer cardId = card.getId();

      Card foundCard = cardRepository.findById(cardId)
          .orElseThrow(() -> new EmptyResultDataAccessException("Card not found", 1));

      AppUser appUser = foundCard.getDeck().getUserId();

      if (authenticatedUserId != appUser.getId()) {
        throw new AccessDeniedException("You are not authorized to update cards for this user.");
      }
    }

    return ResponseEntity.ok(cardService.updateCards(request));
  }

  @PutMapping("/{cardId}")
  public ResponseEntity<CardUpdateResponse> updateCard(
      @RequestBody CardUpdateRequest request,
      @PathVariable int cardId,
      @RequestHeader("Authorization") String token
  ) throws AccessDeniedException {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    Card card = cardRepository.findById(cardId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Card not found", 1));

    int userId = card.getDeck().getUserId().getId();

    if (authenticatedUserId != userId) {
      throw new AccessDeniedException("You are not authorized to update cards for this user.");
    }

    return ResponseEntity.ok(cardService.updateCard(cardId, request));
  }

  @DeleteMapping("/{cardId}")
  public ResponseEntity<Void> deleteCard(
      @PathVariable int cardId,
      @RequestHeader("Authorization") String token
  ) throws AccessDeniedException {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));

    Card card = cardRepository.findById(cardId)
        .orElseThrow(() -> new EmptyResultDataAccessException("Card not found", 1));

    int userId = card.getDeck().getUserId().getId();

    if (authenticatedUserId != userId) {
      throw new AccessDeniedException("You are not authorized to delete cards for this user.");
    }

    cardService.deleteCard(cardId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
