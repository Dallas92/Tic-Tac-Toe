package com.example.tic_tac_toe.client;

import com.example.tic_tac_toe.model.api.CreateGameRequest;
import com.example.tic_tac_toe.model.api.GameInfoDto;
import com.example.tic_tac_toe.model.api.UpdateGameRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "playerClient", path = "/api")
public interface PlayerClient {
  @GetMapping("/games")
  List<GameInfoDto> getGames();

  @GetMapping("/games/{id}")
  GameInfoDto getGame(@PathVariable(name = "id") String id);

  @PostMapping("/games")
  GameInfoDto createGame(@RequestBody CreateGameRequest request);

  @PutMapping("/games/{id}")
  GameInfoDto updateGame(
      @PathVariable(name = "id") String id, @RequestBody UpdateGameRequest request);
}
