package com.example.tic_tac_toe.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.tic_tac_toe.exception.GameBoardCellIsAlreadyFilledException;
import com.example.tic_tac_toe.exception.GameNotFoundException;
import com.example.tic_tac_toe.exception.GameStateException;
import com.example.tic_tac_toe.exception.GameTurnException;
import com.example.tic_tac_toe.model.api.CreateGameRequest;
import com.example.tic_tac_toe.model.api.GameInfoDto;
import com.example.tic_tac_toe.model.api.GameStatus;
import com.example.tic_tac_toe.model.api.UpdateGameRequest;
import com.example.tic_tac_toe.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerComponentTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private GameService gameService;

  @BeforeEach
  void before() {
    reset(gameService);
  }

  @Test
  @SneakyThrows
  void testGetGames() {
    List<GameInfoDto> games =
        List.of(GameInfoDto.builder().id("1").build(), GameInfoDto.builder().id("2").build());
    when(gameService.getGames()).thenReturn(games);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/games").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(games.size()));
  }

  @Test
  @SneakyThrows
  void testGetGame() {
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .status(GameStatus.IN_PROGRESS)
            .playerTurn("sergey")
            .version(1)
            .build();
    when(gameService.getGame(any())).thenReturn(game);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/games/{id}", game.getId())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.player1").value("ivan"))
        .andExpect(jsonPath("$.player2").value("sergey"))
        .andExpect(jsonPath("$.playerTurn").value("sergey"))
        .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
        .andExpect(jsonPath("$.version").value("1"));
  }

  @Test
  @SneakyThrows
  void testGetGameWhenNotFound() {
    when(gameService.getGame(any())).thenThrow(new GameNotFoundException("test exception"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/games/{gameId}", "1")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void testCreateGame() {
    CreateGameRequest createGameRequest = CreateGameRequest.builder().playerId("ivan").build();
    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .status(GameStatus.IN_PROGRESS)
            .playerTurn("sergey")
            .version(1)
            .build();
    when(gameService.createGame(any())).thenReturn(game);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/api/games")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createGameRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.player1").value("ivan"))
        .andExpect(jsonPath("$.player2").value("sergey"))
        .andExpect(jsonPath("$.playerTurn").value("sergey"))
        .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
        .andExpect(jsonPath("$.version").value("1"));
  }

  @Test
  @SneakyThrows
  void testUpdateGame() {
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(1).col(0).version(1).build();

    GameInfoDto game =
        GameInfoDto.builder()
            .id("1")
            .player1("ivan")
            .player2("sergey")
            .status(GameStatus.IN_PROGRESS)
            .playerTurn("sergey")
            .version(2)
            .build();
    when(gameService.updateGame(any(), any())).thenReturn(game);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/games/{id}", game.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGameRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.player1").value("ivan"))
        .andExpect(jsonPath("$.player2").value("sergey"))
        .andExpect(jsonPath("$.playerTurn").value("sergey"))
        .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
        .andExpect(jsonPath("$.version").value("2"));
  }

  @Test
  @SneakyThrows
  void testUpdateGameWhenNotFound() {
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(1).col(0).version(1).build();

    when(gameService.updateGame(any(), any()))
        .thenThrow(new GameNotFoundException("test exception"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/games/{id}", "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGameRequest)))
        .andExpect(status().isNotFound());
  }

  @Test
  @SneakyThrows
  void testUpdateGameWhenGameInFinalStatusOrVersionDontMatch() {
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(1).col(0).version(1).build();

    when(gameService.updateGame(any(), any())).thenThrow(new GameStateException("test exception"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/games/{id}", "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGameRequest)))
        .andExpect(status().isConflict());
  }

  @Test
  @SneakyThrows
  void testUpdateGameWhenNotPlayersTurnOrOtherPlayer() {
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(1).col(0).version(1).build();

    when(gameService.updateGame(any(), any())).thenThrow(new GameTurnException("game not found"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/games/{id}", "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGameRequest)))
        .andExpect(status().isForbidden());
  }

  @Test
  @SneakyThrows
  void testUpdateGameWhenBoardCellIsAlreadyFilled() {
    UpdateGameRequest updateGameRequest =
        UpdateGameRequest.builder().playerId("ivan").row(1).col(0).version(1).build();

    when(gameService.updateGame(any(), any()))
        .thenThrow(new GameBoardCellIsAlreadyFilledException("game not found"));

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/games/{id}", "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateGameRequest)))
        .andExpect(status().isBadRequest());
  }
}
