package com.example.tic_tac_toe.agent;

import com.example.tic_tac_toe.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ScheduledAgent {

  private final PlayerService playerService;

  @Scheduled(fixedRateString = "${player.fixedRate}")
  public void play() {
    try {
      playerService.play();
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
  }
}
