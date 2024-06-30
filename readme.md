# Tic-Tac-Toe game

This project implements a simple Tic Tac Toe game using Spring Boot. <br/>
The game is designed to run two instances of the application which communicate with each other via REST APIs to play the game.

## Description

Tic Tac Toe is a simple two-player game where each player takes turns marking a space in a 3x3 grid. <br/>
The goal is to be the first player to get three of their marks in a row (horizontally, vertically, or diagonally). <br/>
Every player sends requests to each other at some `player.fixedRate` <br/>
Game statistics are stored in Redis. For every new players move there is a validation of consistency (version in request is compared with version of game)

## Prerequisites

- Docker
- Docker Compose
- Java 17 or later
- Maven

## Formatting

We use Google FMT formatting for Java code <br/>
Run `mvn fmt:format` command before making any commit to GIT

## Getting Started

### Running applications locally

1. Run Redis in Docker

    `docker run --name redis -p 6379:6379 -d redis`


2. Run 2 instances of app on different ports with environment variables

    possible environment variables for 1st instance (-Dserver.port=8081):
    `PLAYER_ID=ivan;
    PLAYER_FIXED_RATE=4000;
    OPPONENT_PLAYER_URL=http://localhost:8082;`

    possible environment variables for 2nd instance (-Dserver.port=8082):
    `PLAYER_ID=sergey;
    PLAYER_FIXED_RATE=6000;
    OPPONENT_PLAYER_URL=http://localhost:8081;`

### Running applications in Docker

1. Build docker image
    
    `docker build -t tic-tac-toe .`

2. Run docker compose

    `docker-compose up -d`

## Postman collection

Import Postman collection to send requests to application.

Use `GET /api/games` endpoint for monitoring progress of all games. Players do moves every 4-6 seconds
