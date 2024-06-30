package com.example.tic_tac_toe.service;

import com.example.tic_tac_toe.model.api.GameInfoDto;
import com.example.tic_tac_toe.model.api.UpdateGameRequest;

public interface TicTacToeService {
  GameInfoDto createGame(String player1Id, String player2Id);

  GameInfoDto makeAction(GameInfoDto gameInfo, UpdateGameRequest request);
}
