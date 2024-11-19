package com.brainplus.growMind.deck;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Integer> {

  List<Deck> findByUserId_Id(int userId);
}
