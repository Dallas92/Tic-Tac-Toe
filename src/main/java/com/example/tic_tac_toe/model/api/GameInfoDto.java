package com.example.tic_tac_toe.model.api;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameInfoDto implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;
  private char[][] board;
  private String player1;
  private String player2;
  private String playerTurn;
  private GameStatus status;
  private Integer version;
}
