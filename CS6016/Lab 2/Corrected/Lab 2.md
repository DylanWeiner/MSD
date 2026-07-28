Players [__ID (integer)__, name (string), Elo (integer)]
Events [__name (string), date (date)__, site (string)]
Games [__moves (string)__, winnerID (integer)]

CREATE TABLE Players(
	UNIQUE ID INT PRIMARY KEY,
	Name (string),
	Elo (integer),
)

CREATE TABLE Events(
	UNIQUE ID INT PRIMARY KEY,
	Name STRING SECONDARY KEY,
	Site (string)
	Game (blackID (integer), white ID (integer), moves (string), winnerID (integer))
)

CREATE TABLE Games(
	Moves STRING PRIMARY KEY,
	FOREIGN KEY winnerID REFERENCES players(id)
)

![Lab 2](Lab2.png)
