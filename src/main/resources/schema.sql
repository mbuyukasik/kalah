DROP TABLE IF EXISTS PLAYER;
 
CREATE TABLE PLAYER (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL
);

DROP TABLE IF EXISTS GAME;
 
CREATE TABLE GAME (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  number_of_stones INT NOT NULL,
  code VARCHAR(20) NOT NULL,
  player1_id INT NOT NULL,
  player2_id INT NOT NULL,
  next_player_id INT,
  last_move_seq INT,
  pit_status VARCHAR(200) NOT NULL,
  status VARCHAR(20),
  player1_score INT,
  player2_score INT,
  winner_player_id INT
);

DROP TABLE IF EXISTS MOVE;
 
CREATE TABLE MOVE (
  id INT AUTO_INCREMENT  PRIMARY KEY,  
  game_id INT NOT NULL,
  player_id INT NOT NULL,
  pit_id INT NOT NULL,
  seq INT NOT NULL,
  pit_status VARCHAR(200) NOT NULL
);