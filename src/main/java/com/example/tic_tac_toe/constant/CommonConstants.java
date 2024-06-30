package com.example.tic_tac_toe.constant;

import com.example.tic_tac_toe.model.api.GameStatus;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonConstants {
  public static final List<GameStatus> FINAL_GAME_STATUSES =
      List.of(GameStatus.DRAW, GameStatus.PLAYER1_WINS, GameStatus.PLAYER2_WINS);

  public static class WIN_BOARD_CELL_COMBINATIONS {
    private static final int[][] B1 = new int[][] {{0, 0}, {0, 1}, {0, 2}};
    private static final int[][] B2 = new int[][] {{1, 0}, {1, 1}, {1, 2}};
    private static final int[][] B3 = new int[][] {{2, 0}, {2, 1}, {2, 2}};
    private static final int[][] B4 = new int[][] {{0, 0}, {1, 0}, {2, 0}};
    private static final int[][] B5 = new int[][] {{0, 1}, {1, 1}, {2, 1}};
    private static final int[][] B6 = new int[][] {{0, 2}, {1, 2}, {2, 2}};
    private static final int[][] B7 = new int[][] {{0, 0}, {1, 1}, {2, 2}};
    private static final int[][] B8 = new int[][] {{0, 2}, {1, 1}, {2, 0}};
    public static final List<int[][]> POSITIONS = List.of(B1, B2, B3, B4, B5, B6, B7, B8);
  }
}
