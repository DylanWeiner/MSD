Commands to build dealership tables:
```
CREATE TABLE Dealer(
	SSN INTEGER PRIMARY KEY,
	Name VARCHAR NOT NULL
);
```
```
CREATE TABLE Car(
	VIN VARCHAR PRIMARY KEY,
	Make VARCHAR NOT NULL,
	Model VARCHAR NOT NULL,
	Year INTEGER NOT NULL,
	Color VARCHAR NOT NULL
);
```
```
CREATE TABLE Inventory(
	VIN VARCHAR REFERENCES Car(VIN),
	SSN INTEGER REFERENCES Dealer(SSN),
	PRIMARY KEY (SSN, VIN)
);
```
```
INSERT INTO Car VALUES
	('AX10', 'Toyota', 'Tacoma', 2008, 'Red'),
	('AX11', 'Toyota', 'Tacoma', 1999, 'Green'),
	('BX10', 'Tesla', 'Model 3', 2018, 'White'),
	('CX10', 'Subaru', 'WRX', 2016, 'Blue'),
	('DX10', 'Ford', 'F150', 2004, 'Red');
```
```
INSERT INTO Dealer VALUES
	(432-67-0000, 'Arnold'),
	(432-67-0001, 'Hannah'),
	(432-67-0002, 'Steve');
```
```
INSERT INTO Inventory VALUES
	('AX10', 432-67-0000),
	('AX11', 432-67-0000),
	('AX10', 432-67-0001),
	('CX10', 432-67-0001),
	('BX10', 432-67-0002);
```

Part 3:
- Get the Titles of all books by `<Author>`
	SELECT title FROM titles WHERE author=`<Author>`;
- Get Serial numbers of all books by `<Author>`
	SELECT inventory.serial
	FROM titles
	NATURAL JOIN inventory
	WHERE titles.author=`<Author>`;
- Get the Titles of all books checked out by `<Patron’s name>`
	SELECT titles.title
	FROM titles
	NATURAL JOIN inventory
	NATURAL JOIN checkedout
	NATURAL JOIN patrons
	WHERE patrons.name=`<Name>`;
- Get phone number(s) of anyone with `<Title>` checked out
	SELECT phones.phone
	FROM phones
	NATURAL JOIN patrons
	NATURAL JOIN checkedout
	NATURAL JOIN inventory
	NATURAL JOIN titles
	WHERE titles.title=`<Title>`;

Part 4:
1. Find the names of any player with an Elo rating of 2850 or higher.
	SELECT players.name WHERE Elo >= 2850;
2. Find the names of any player who has ever played a game as white.
	SELECT players.name
	FROM players
	JOIN games
	ON players.pid  = games.whiteplayer;
3. Find the names of any player who has ever won a game as white.
	SELECT players.name
	FROM players
	JOIN games
	ON players.pid  = games.whiteplayer
	WHERE games.result='W';
4. Find the names of any player who played any games in 2018.
	SELECT players.name
	FROM players
	JOIN games
	ON players.pid  = games.whiteplayer
	OR players.pid = games.blackplayer
	NATURAL JOIN events
	WHERE events.date >= '2018-01-01' AND events.date <= '2018-12-31';
5. Find the names and dates of any event in which Magnus Carlsen lost a game.
	SELECT events.name, events.date
	FROM events
	NATURAL JOIN games
	JOIN players
	ON games.whiteplayer = players.pid
	OR games.blackplayer = players.pid
	WHERE players.name = 'Carlsen, Magnus' AND ((players.pid=games.whiteplayer AND result = 'B') OR (players.pid=games.blackplayer AND result = 'W'));
6. Find the names of all opponents of Magnus Carlsen. An opponent is someone who he has played a game against. Hint: Both Magnus and his opponents could play as white or black.
	WITH magnusBlackOpps AS (
		SELECT bp.name AS oppName
		FROM players wp
		JOIN games
		ON wp.pid = games.whiteplayer
		JOIN players bp
		ON bp.pid = games.blackplayer
		WHERE wp.name = 'Carlsen, Magnus'
	 ),
	 magnusWhiteOpps AS (
		SELECT wp.name AS oppName
		FROM players wp
		JOIN games
		ON wp.pid = games.whiteplayer
		JOIN players bp
		ON bp.pid = games.blackplayer
		WHERE bp.name = 'Carlsen, Magnus'
	 )
	 SELECT * FROM magnusBlackOpps UNION  SELECT * FROM magnusWhiteOpps