package com.example.tic_tac_toe.controller;

import com.example.tic_tac_toe.model.api.CreateGameRequest;
import com.example.tic_tac_toe.model.api.GameInfoDto;
import com.example.tic_tac_toe.model.api.UpdateGameRequest;
import com.example.tic_tac_toe.service.GameService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GameController {

  private final GameService gameService;

  @PostMapping("/games")
  public ResponseEntity<GameInfoDto> createGame(@RequestBody @Valid CreateGameRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(request));
  }

  @GetMapping("/games")
  public ResponseEntity<List<GameInfoDto>> getGames() {
    return ResponseEntity.ok(gameService.getGames());
  }

  @GetMapping("/games/{id}")
  public ResponseEntity<GameInfoDto> getGame(@PathVariable String id) {
    return ResponseEntity.ok(gameService.getGame(id));
  }

  @PutMapping("/games/{id}")
  public ResponseEntity<GameInfoDto> updateGame(
      @PathVariable String id, @RequestBody @Valid UpdateGameRequest request) {
    return ResponseEntity.ok().body(gameService.updateGame(id, request));
  }
}
