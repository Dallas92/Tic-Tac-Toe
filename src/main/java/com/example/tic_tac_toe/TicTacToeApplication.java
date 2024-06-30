package com.example.tic_tac_toe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
@EnableFeignClients
public class TicTacToeApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicTacToeApplication.class, args);
  }
}
