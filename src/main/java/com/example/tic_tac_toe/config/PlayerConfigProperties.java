package com.example.tic_tac_toe.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "player")
@Configuration
@Getter
@Setter
public class PlayerConfigProperties {
  private String id;
}
