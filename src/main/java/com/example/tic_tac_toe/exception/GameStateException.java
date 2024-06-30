package com.example.tic_tac_toe.exception;

public class GameStateException extends RuntimeException {

  public GameStateException(String message) {
    super(message);
  }
}
