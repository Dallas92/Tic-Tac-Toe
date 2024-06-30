package com.example.tic_tac_toe.service.impl;

import static org.apache.commons.lang3.StringUtils.equalsAny;

import com.example.tic_tac_toe.client.PlayerClient;
import com.example.tic_tac_toe.config.PlayerConfigProperties;
import com.example.tic_tac_toe.model.api.CreateGameRequest;
import com.example.tic_tac_toe.model.api.GameInfoDto;
import com.example.tic_tac_toe.model.api.GameStatus;
import com.example.tic_tac_toe.model.api.UpdateGameRequest;
import com.example.tic_tac_toe.service.PlayerService;
import com.example.tic_tac_toe.util.TicTacToeBoardUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

  private final PlayerConfigProperties playerConfigProperties;
  private final PlayerClient playerClient;

  @Override
  public void play() {
    List<GameInfoDto> games =
        playerClient.getGames().stream()
            .filter(
                x ->
                    x.getStatus().equals(GameStatus.IN_PROGRESS)
                        && equalsAny(
                            playerConfigProperties.getId(), x.getPlayer1(), x.getPlayer2()))
            .toList();

    if (CollectionUtils.isEmpty(games)) {
      log.info(
          "player (id=%s) not found any active game".formatted(playerConfigProperties.getId()));
      CreateGameRequest createGameRequest =
          CreateGameRequest.builder().playerId(playerConfigProperties.getId()).build();
      GameInfoDto game = playerClient.createGame(createGameRequest);
      log.info(
          "player (id=%s) created game (id=%s)"
              .formatted(playerConfigProperties.getId(), game.getId()));
    } else {
      for (GameInfoDto game : games) {
        log.info(
            "player (id=%s) found any active game (id=%s)"
                .formatted(playerConfigProperties.getId(), game.getId()));
        if (game.getPlayerTurn().equals(playerConfigProperties.getId())) {
          int[] cell = TicTacToeBoardUtil.getRandomFreeCell(game.getBoard());
          UpdateGameRequest updateGameRequest =
              UpdateGameRequest.builder()
                  .playerId(playerConfigProperties.getId())
                  .version(game.getVersion())
                  .row(cell[0])
                  .col(cell[1])
                  .build();
          playerClient.updateGame(game.getId(), updateGameRequest);
          log.info(
              "player (id=%s) does move in game (id=%s) with cell(row=%s, col=%s)"
                  .formatted(
                      playerConfigProperties.getId(),
                      game.getId(),
                      updateGameRequest.row(),
                      updateGameRequest.col()));
        } else {
          log.info(
              "player (id=%s) waits for the next move of opponent (id=%s) in game (id=%s)"
                  .formatted(playerConfigProperties.getId(), game.getPlayerTurn(), game.getId()));
        }
      }
    }
  }
}
