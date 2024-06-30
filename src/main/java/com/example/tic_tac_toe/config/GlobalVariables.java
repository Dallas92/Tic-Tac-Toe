package com.example.tic_tac_toe.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GlobalVariables {
  private String gameId;
  private Integer gameVersion;
}
