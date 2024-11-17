package com.brainplus.growMind.deck;

import com.brainplus.growMind.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Integer> {

  List<Deck> findByUserId_Id(int userId);
}
