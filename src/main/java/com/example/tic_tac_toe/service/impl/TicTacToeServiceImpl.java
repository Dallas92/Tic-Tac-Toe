package com.example.tic_tac_toe.service.impl;

import static com.example.tic_tac_toe.constant.CommonConstants.FINAL_GAME_STATUSES;
import static org.apache.commons.lang3.StringUtils.equalsAny;

import com.example.tic_tac_toe.constant.CommonConstants;
import com.example.tic_tac_toe.exception.GameBoardCellIsAlreadyFilledException;
import com.example.tic_tac_toe.exception.GameStateException;
import com.example.tic_tac_toe.exception.GameTurnException;
import com.example.tic_tac_toe.model.api.GameInfoDto;
import com.example.tic_tac_toe.model.api.GameStatus;
import com.example.tic_tac_toe.model.api.UpdateGameRequest;
import com.example.tic_tac_toe.service.TicTacToeService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TicTacToeServiceImpl implements TicTacToeService {

  public static final char EMPTY_BOARD_CELL = '-';
  private static final char PLAYER1_BOARD_CELL = 'o';
  private static final char PLAYER2_BOARD_CELL = 'x';

  @Override
  public GameInfoDto createGame(String player1Id, String player2Id) {
    String[] players = shufflePlayers(player1Id, player2Id);
    char[][] board = generateBoard();

    return GameInfoDto.builder()
        .id(UUID.randomUUID().toString())
        .board(board)
        .player1(players[0])
        .player2(players[1])
        .playerTurn(players[0])
        .status(GameStatus.IN_PROGRESS)
        .version(1)
        .build();
  }

  @Override
  public GameInfoDto makeAction(GameInfoDto gameInfo, UpdateGameRequest request) {
    validate(gameInfo, request);
    fillCell(gameInfo, request);
    return gameInfo;
  }

  private String[] shufflePlayers(String player1Name, String player2Name) {
    List<String> players = Arrays.asList(player1Name, player2Name);
    Collections.shuffle(players);
    return players.toArray(new String[2]);
  }

  private char[][] generateBoard() {
    return new char[][] {
      {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
      {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
      {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
    };
  }

  private void validate(GameInfoDto gameInfo, UpdateGameRequest request) {
    if (FINAL_GAME_STATUSES.contains(gameInfo.getStatus())) {
      throw new GameStateException("game (id=%s) is in final status".formatted(gameInfo.getId()));
    } else if (!gameInfo.getVersion().equals(request.version())) {
      throw new GameStateException(
          "player (id=%s) action was for previous version of the game"
              .formatted(request.playerId()));
    } else if (!equalsAny(request.playerId(), gameInfo.getPlayer1(), gameInfo.getPlayer2())) {
      throw new GameTurnException(
          "player (id=%s) is not allowed to make action because he is not one the players"
              .formatted(request.playerId()));
    } else if (!gameInfo.getPlayerTurn().equals(request.playerId())) {
      throw new GameTurnException(
          "player (id=%s) is not allowed to make action because it's turn for other player"
              .formatted(request.playerId()));
    } else if (gameInfo.getBoard()[request.row()][request.col()] != EMPTY_BOARD_CELL) {
      throw new GameBoardCellIsAlreadyFilledException(
          "cell (row=%s, col=%s) is already filled with %s"
              .formatted(
                  request.row(), request.col(), gameInfo.getBoard()[request.row()][request.col()]));
    }
  }

  private GameInfoDto fillCell(GameInfoDto gameInfo, UpdateGameRequest request) {
    if (gameInfo.getPlayer1().equals(request.playerId())) {
      gameInfo.getBoard()[request.row()][request.col()] = PLAYER1_BOARD_CELL;
      gameInfo.setPlayerTurn(gameInfo.getPlayer2());
    } else {
      gameInfo.getBoard()[request.row()][request.col()] = PLAYER2_BOARD_CELL;
      gameInfo.setPlayerTurn(gameInfo.getPlayer1());
    }

    gameInfo.setVersion(gameInfo.getVersion() + 1);

    checkBoard(gameInfo);

    return gameInfo;
  }

  private void checkBoard(GameInfoDto gameInfoDto) {
    checkForAnyPlayerWinner(gameInfoDto);
    checkForNoPlayerWinner(gameInfoDto);
  }

  private void checkForAnyPlayerWinner(GameInfoDto gameInfoDto) {
    for (int[][] winPositions : CommonConstants.WIN_BOARD_CELL_COMBINATIONS.POSITIONS) {
      if (gameInfoDto.getBoard()[winPositions[0][0]][winPositions[0][1]] == EMPTY_BOARD_CELL) {
        break;
      } else if (gameInfoDto.getBoard()[winPositions[0][0]][winPositions[0][1]]
              == gameInfoDto.getBoard()[winPositions[1][0]][winPositions[1][1]]
          && gameInfoDto.getBoard()[winPositions[0][0]][winPositions[0][1]]
              == gameInfoDto.getBoard()[winPositions[2][0]][winPositions[2][1]]) {
        if (gameInfoDto.getBoard()[winPositions[0][0]][winPositions[0][1]] == PLAYER1_BOARD_CELL) {
          gameInfoDto.setStatus(GameStatus.PLAYER1_WINS);
        } else {
          gameInfoDto.setStatus(GameStatus.PLAYER2_WINS);
        }
      }
    }
  }

  private void checkForNoPlayerWinner(GameInfoDto gameInfoDto) {
    if (gameInfoDto.getStatus().equals(GameStatus.IN_PROGRESS)) {
      boolean draw = true;
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
          if (generateBoard()[i][j] == EMPTY_BOARD_CELL) {
            draw = false;
            break;
          }
        }
      }

      if (draw) {
        gameInfoDto.setStatus(GameStatus.DRAW);
      }
    }
  }
}
