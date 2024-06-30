package com.example.tic_tac_toe.service;

import com.example.tic_tac_toe.model.api.CreateGameRequest;
import com.example.tic_tac_toe.model.api.GameInfoDto;
import com.example.tic_tac_toe.model.api.UpdateGameRequest;
import java.util.List;

public interface GameService {
  GameInfoDto createGame(CreateGameRequest request);

  List<GameInfoDto> getGames();

  GameInfoDto getGame(String id);

  GameInfoDto updateGame(String id, UpdateGameRequest request);
}
