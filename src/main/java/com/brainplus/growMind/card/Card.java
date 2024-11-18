package com.brainplus.growMind.card;

import com.brainplus.growMind.deck.Deck;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="card")
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private int id;

  @Column(name="front_side", nullable = false)
  private String frontSide;

  @Column(name="back_side", nullable = false)
  private String backSide;

  @Column(name="difficulty")
  private int difficulty;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {
          CascadeType.PERSIST,
          CascadeType.DETACH,
          CascadeType.MERGE,
          CascadeType.REFRESH
      }
  )
  @JoinTable(
      name="deck_card",
      joinColumns = @JoinColumn(name="card_id"),
      inverseJoinColumns = @JoinColumn(name="deck_id")
  )
  private List<Deck> decks;

  @Column(name="updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  @PreUpdate
  public void updateTimestamp() {
    this.updatedAt = LocalDateTime.now();
  }
}
