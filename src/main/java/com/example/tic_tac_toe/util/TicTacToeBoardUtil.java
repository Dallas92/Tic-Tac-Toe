package com.example.tic_tac_toe.util;

import static com.example.tic_tac_toe.service.impl.TicTacToeServiceImpl.EMPTY_BOARD_CELL;

import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TicTacToeBoardUtil {

  public int[] getRandomFreeCell(char[][] board) {
    while (true) {
      Random random = new Random();
      int row = random.nextInt(2);
      int col = random.nextInt(2);

      if (board[row][col] == EMPTY_BOARD_CELL) {
        return new int[] {row, col};
      }
    }
  }
}
