package com.example.tic_tac_toe.exception;

public class GameTurnException extends RuntimeException {

  public GameTurnException(String message) {
    super(message);
  }
}
