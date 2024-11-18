package com.brainplus.growMind.card;

import com.brainplus.growMind.config.JwtService;
import com.brainplus.growMind.deck.DeckRepository;
import com.brainplus.growMind.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

  private final CardService cardService;
  private final DeckRepository deckRepository;
  private final JwtService jwtService;

  @PostMapping
  public ResponseEntity<CardCreationResponse> createCardAndAddToDecks(
      @RequestBody CardCreationRequest request,
      @RequestHeader("Authorization") String token
  ) {
    int authenticatedUserId = jwtService.extractUserIdFromToken(token.replace("Bearer ", ""));
    List<Integer> deckIds = request.getDeckIds();

    for (Integer deckId : deckIds) {
      AppUser appUser = deckRepository.findById(deckId)
          .orElseThrow(() -> new RuntimeException("Deck not found")).getUserId();

      if (authenticatedUserId != appUser.getId()) {
        throw new RuntimeException("Not Authorized.");
      }
    }

    return ResponseEntity.ok(cardService.createCardAndAddToDecks(request));
  }
}
