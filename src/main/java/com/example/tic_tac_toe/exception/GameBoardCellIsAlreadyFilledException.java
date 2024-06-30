package com.example.tic_tac_toe.exception;

public class GameBoardCellIsAlreadyFilledException extends RuntimeException {

  public GameBoardCellIsAlreadyFilledException(String message) {
    super(message);
  }
}
