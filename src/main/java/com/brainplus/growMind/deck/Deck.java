package com.brainplus.growMind.deck;

import com.brainplus.growMind.card.Card;
import com.brainplus.growMind.user.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="deck")
public class Deck {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private int id;

  @Column(name="name", nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name= "user_id", nullable = true)
  @JsonIgnore
  private AppUser userId;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL
  )
  @JoinTable(
      name="deck_card",
      joinColumns = @JoinColumn(name="deck_id"),
      inverseJoinColumns = @JoinColumn(name="card_id")
  )
  private List<Card> cards;

  @Column(name="updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
  @PreUpdate
  public void updateTimestamp() {
    this.updatedAt = LocalDateTime.now();
  }

}
