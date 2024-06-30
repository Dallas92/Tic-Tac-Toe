package com.example.tic_tac_toe.exception;

public class GameNotFoundException extends RuntimeException {

  public GameNotFoundException(String message) {
    super(message);
  }
}
