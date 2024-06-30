package com.example.tic_tac_toe.service;

import static com.example.tic_tac_toe.service.impl.TicTacToeServiceImpl.*;
import static org.apache.commons.lang3.StringUtils.equalsAny;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.tic_tac_toe.exception.GameBoardCellIsAlreadyFilledException;
import com.example.tic_tac_toe.exception.GameStateException;
import com.example.tic_tac_toe.exception.GameTurnException;
import com.example.tic_tac_toe.model.api.GameInfoDto;
import com.example.tic_tac_toe.model.api.GameStatus;
import com.example.tic_tac_toe.model.api.UpdateGameRequest;
import com.example.tic_tac_toe.service.impl.TicTacToeServiceImpl;
import org.junit.jupiter.api.Test;

class TicTacToeServiceUnitTest {

  private final TicTacToeService ticTacToeService = new TicTacToeServiceImpl();

  @Test
  void testCreateNewGame() {
    String player1Id = "ivan";
    String player2Id = "sergey";

    GameInfoDto result = ticTacToeService.createGame(player1Id, player2Id);
    assertThat(result).isNotNull();
    assertThat(result.getId()).isNotEmpty();
    assertThat(result.getPlayer1()).isNotEqualTo(result.getPlayer2());
    assertTrue(equalsAny(result.getPlayer1(), player1Id, player2Id));
    assertTrue(equalsAny(result.getPlayer2(), player1Id, player2Id));
    assertTrue(equalsAny(result.getPlayerTurn(), player1Id, player2Id));
    assertThat(result.getBoard())
        .isEqualTo(
            new char[][] {
              {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
              {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
              {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
            });
    assertThat(result.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    assertThat(result.getVersion()).isEqualTo(1);
  }

  @Test
  void testPlayer1MakeAction() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .playerTurn("ivan")
            .board(
                new char[][] {
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
                })
            .status(GameStatus.IN_PROGRESS)
            .version(1)
            .build();
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(1).col(1).version(1).build();
    GameInfoDto result = ticTacToeService.makeAction(game, updateGameRequest);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(game.getId());
    assertThat(result.getPlayer1()).isEqualTo("ivan");
    assertThat(result.getPlayer2()).isEqualTo("sergey");
    assertThat(result.getPlayerTurn()).isEqualTo("sergey");
    assertThat(result.getBoard())
        .isEqualTo(
            new char[][] {
              {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
              {EMPTY_BOARD_CELL, PLAYER1_BOARD_CELL, EMPTY_BOARD_CELL},
              {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
            });
    assertThat(result.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    assertThat(result.getVersion()).isEqualTo(2);
  }

  @Test
  void testPlayer2MakeAction() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .playerTurn("sergey")
            .board(
                new char[][] {
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
                })
            .status(GameStatus.IN_PROGRESS)
            .version(1)
            .build();
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("sergey").row(1).col(1).version(1).build();
    GameInfoDto result = ticTacToeService.makeAction(game, updateGameRequest);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(game.getId());
    assertThat(result.getPlayer1()).isEqualTo("ivan");
    assertThat(result.getPlayer2()).isEqualTo("sergey");
    assertThat(result.getPlayerTurn()).isEqualTo("ivan");
    assertThat(result.getBoard())
        .isEqualTo(
            new char[][] {
              {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
              {EMPTY_BOARD_CELL, PLAYER2_BOARD_CELL, EMPTY_BOARD_CELL},
              {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
            });
    assertThat(result.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    assertThat(result.getVersion()).isEqualTo(2);
  }

  @Test
  void testPlayerMakeActionWhenGameInFinalStatus() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .playerTurn("ivan")
            .board(
                new char[][] {
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
                })
            .status(GameStatus.DRAW)
            .version(1)
            .build();
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(1).col(1).version(1).build();
    assertThrows(
        GameStateException.class, () -> ticTacToeService.makeAction(game, updateGameRequest));
  }

  @Test
  void testPlayerMakeActionWhenRequestVersionAndGameVersionDontMatch() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .playerTurn("ivan")
            .board(
                new char[][] {
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
                })
            .status(GameStatus.IN_PROGRESS)
            .version(2)
            .build();
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(1).col(1).version(4).build();
    assertThrows(
        GameStateException.class, () -> ticTacToeService.makeAction(game, updateGameRequest));
  }

  @Test
  void testPlayerMakeActionWhenUnknownPlayer() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .playerTurn("ivan")
            .board(
                new char[][] {
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
                })
            .status(GameStatus.IN_PROGRESS)
            .version(2)
            .build();
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("unknown").row(1).col(1).version(2).build();
    assertThrows(
        GameTurnException.class, () -> ticTacToeService.makeAction(game, updateGameRequest));
  }

  @Test
  void testPlayerMakeActionWhenNotPlayersTurn() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .playerTurn("ivan")
            .board(
                new char[][] {
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
                })
            .status(GameStatus.IN_PROGRESS)
            .version(2)
            .build();
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("sergey").row(1).col(1).version(2).build();
    assertThrows(
        GameTurnException.class, () -> ticTacToeService.makeAction(game, updateGameRequest));
  }

  @Test
  void testPlayerMakeActionWhenBoardCellIsAlreadyFilled() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .playerTurn("ivan")
            .board(
                new char[][] {
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, PLAYER1_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
                })
            .status(GameStatus.IN_PROGRESS)
            .version(2)
            .build();
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(1).col(1).version(2).build();
    assertThrows(
        GameBoardCellIsAlreadyFilledException.class,
        () -> ticTacToeService.makeAction(game, updateGameRequest));
  }

  @Test
  void testPlayer1MoveLeadsToWin() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .playerTurn("ivan")
            .board(
                new char[][] {
                  {PLAYER1_BOARD_CELL, PLAYER1_BOARD_CELL, EMPTY_BOARD_CELL},
                  {EMPTY_BOARD_CELL, PLAYER2_BOARD_CELL, EMPTY_BOARD_CELL},
                  {PLAYER2_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
                })
            .status(GameStatus.IN_PROGRESS)
            .version(5)
            .build();
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(0).col(2).version(5).build();
    GameInfoDto result = ticTacToeService.makeAction(game, updateGameRequest);

    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(GameStatus.PLAYER1_WINS);
    assertThat(result.getBoard())
        .isEqualTo(
            new char[][] {
              {PLAYER1_BOARD_CELL, PLAYER1_BOARD_CELL, PLAYER1_BOARD_CELL},
              {EMPTY_BOARD_CELL, PLAYER2_BOARD_CELL, EMPTY_BOARD_CELL},
              {PLAYER2_BOARD_CELL, EMPTY_BOARD_CELL, EMPTY_BOARD_CELL}
            });
    assertThat(result.getPlayerTurn()).isEqualTo("sergey");
    assertThat(result.getVersion()).isEqualTo(6);
  }

  @Test
  void testPlayer1MoveLeadsToDraw() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .playerTurn("ivan")
            .board(
                new char[][] {
                  {PLAYER1_BOARD_CELL, PLAYER1_BOARD_CELL, PLAYER2_BOARD_CELL},
                  {PLAYER2_BOARD_CELL, PLAYER2_BOARD_CELL, PLAYER1_BOARD_CELL},
                  {PLAYER1_BOARD_CELL, PLAYER2_BOARD_CELL, EMPTY_BOARD_CELL}
                })
            .status(GameStatus.IN_PROGRESS)
            .version(8)
            .build();
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(2).col(2).version(8).build();
    GameInfoDto result = ticTacToeService.makeAction(game, updateGameRequest);

    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(GameStatus.DRAW);
    assertThat(result.getBoard())
        .isEqualTo(
            new char[][] {
              {PLAYER1_BOARD_CELL, PLAYER1_BOARD_CELL, PLAYER2_BOARD_CELL},
              {PLAYER2_BOARD_CELL, PLAYER2_BOARD_CELL, PLAYER1_BOARD_CELL},
              {PLAYER1_BOARD_CELL, PLAYER2_BOARD_CELL, PLAYER1_BOARD_CELL}
            });
    assertThat(result.getPlayerTurn()).isEqualTo("sergey");
    assertThat(result.getVersion()).isEqualTo(9);
  }
}
