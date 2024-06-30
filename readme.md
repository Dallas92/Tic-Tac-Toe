## 1. Project info

## 2. How to run

### 2.1 Locally

`docker run --name redis -p 6379:6379 -d redis`

run 2 instances of app on different ports with environment variables

possible environment variables for 1st instance (-Dserver.port=8081):
PLAYER_ID=ivan;
PLAYER_FIXED_RATE=4000;
OPPONENT_PLAYER_URL=http://localhost:8082;

possible environment variables for 2nd instance (-Dserver.port=8082):
PLAYER_ID=sergey;
PLAYER_FIXED_RATE=6000;
OPPONENT_PLAYER_URL=http://localhost:8081;

### 2.2 In docker
`docker build -t tic-tac-toe .`

`docker-compose up -d`
