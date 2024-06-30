package com.example.tic_tac_toe.model.api;

import lombok.Builder;

@Builder(toBuilder = true)
public record CreateGameRequest(String playerId) {}
