package com.example.tic_tac_toe.model.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateGameRequest(
    @NotEmpty String playerId,
    @NotNull @Min(value = 0) @Max(value = 2) Integer row,
    @NotNull @Min(value = 0) @Max(value = 2) Integer col,
    @NotNull @Min(value = 1) Integer version) {}
