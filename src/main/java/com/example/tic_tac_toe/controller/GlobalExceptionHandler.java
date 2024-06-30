package com.example.tic_tac_toe.controller;

import com.example.tic_tac_toe.exception.GameBoardCellIsAlreadyFilledException;
import com.example.tic_tac_toe.exception.GameNotFoundException;
import com.example.tic_tac_toe.exception.GameStateException;
import com.example.tic_tac_toe.exception.GameTurnException;
import com.example.tic_tac_toe.model.api.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(GameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(GameNotFoundException ex) {
    return generateError(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(GameStateException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(GameStateException ex) {
    return generateError(ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(GameTurnException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(GameTurnException ex) {
    return generateError(ex.getMessage(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(GameBoardCellIsAlreadyFilledException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      GameBoardCellIsAlreadyFilledException ex) {
    return generateError(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
    return generateError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<ErrorResponse> generateError(String message, HttpStatus httpStatus) {
    return new ResponseEntity<>(new ErrorResponse(message), httpStatus);
  }
}
