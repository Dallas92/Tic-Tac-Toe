package com.example.tic_tac_toe.service.impl;

import com.example.tic_tac_toe.config.PlayerConfigProperties;
import com.example.tic_tac_toe.exception.GameNotFoundException;
import com.example.tic_tac_toe.model.api.CreateGameRequest;
import com.example.tic_tac_toe.model.api.GameInfoDto;
import com.example.tic_tac_toe.model.api.UpdateGameRequest;
import com.example.tic_tac_toe.service.GameService;
import com.example.tic_tac_toe.service.TicTacToeService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

  private static final Duration REDIS_ITEM_TTL = Duration.ofMinutes(30);

  private final TicTacToeService ticTacToeService;
  private final PlayerConfigProperties playerConfigProperties;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public GameInfoDto createGame(CreateGameRequest request) {
    GameInfoDto gameInfo =
        ticTacToeService.createGame(playerConfigProperties.getId(), request.playerId());
    redisTemplate.opsForValue().set(gameInfo.getId(), gameInfo, REDIS_ITEM_TTL);
    return gameInfo;
  }

  @Override
  public List<GameInfoDto> getGames() {
    Set<String> keys = redisTemplate.keys("*");
    List<GameInfoDto> list = new ArrayList<>();

    if (keys != null) {
      for (String key : keys) {
        list.add((GameInfoDto) redisTemplate.opsForValue().get(key));
      }
    }
    return list;
  }

  @Override
  public GameInfoDto getGame(String id) {
    return Optional.ofNullable((GameInfoDto) redisTemplate.opsForValue().get(id))
        .orElseThrow(() -> new GameNotFoundException("game(id=%s) not found".formatted(id)));
  }

  @Override
  public GameInfoDto updateGame(String id, UpdateGameRequest request) {
    GameInfoDto gameInfo = getGame(id);
    ticTacToeService.makeAction(gameInfo, request);
    redisTemplate.opsForValue().set(gameInfo.getId(), gameInfo, REDIS_ITEM_TTL);
    return gameInfo;
  }
}
