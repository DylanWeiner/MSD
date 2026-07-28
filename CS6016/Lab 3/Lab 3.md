## Part 1 - Joins

Consider the following relations:
### T1

| A   | Q   | R   |
| --- | --- | --- |
| 20  | a   | 5   |
| 25  | b   | 8   |
| 35  | a   | 6   |

### T2

| A   | B   | C   |
| --- | --- | --- |
| 20  | b   | 6   |
| 45  | c   | 3   |
| 20  | b   | 5   |

Provide the relations that result from the following queries. Your relations should be in the form of a table, and should include the schema.
1. $T1 \bowtie_{T1.A = T2.A} T2$

| A   | Q   | R   | A   | B   | C   |
| --- | --- | --- | --- | --- | --- |
| 20  | a   | 5   | 20  | b   | 6   |
| 20  | a   | 5   | 20  | b   | 5   |
[__T1.A (integer)__, Q (char), R (integer), T2.A (integer), B (char), C (integer)]
2. $T1 \bowtie_{T1.Q = T2.B} T2$

| A   | Q   | R   | A   | B   | C   |
| --- | --- | --- | --- | --- | --- |
| 25  | b   | 8   | 20  | b   | 6   |
| 25  | b   | 8   | 20  | b   | 5   |
[__T1.A (integer)__, Q (char), R (integer), T2.A (integer), B (char), C (integer)]
3. $T1 \bowtie T2$

| A   | Q   | R   | B   | C   |
| --- | --- | --- | --- | --- |
| 20  | a   | 5   | b   | 6   |
| 20  | a   | 5   | b   | 5   |
[__A (integer)__, Q (char), R (integer), B (char), C (integer)]
4. $T1 \bowtie_{T1.A = T2.A \wedge T1.R = T2.C} T2$

| A   | Q   | R   | A   | B   | C   |
| --- | --- | --- | --- | --- | --- |
| 20  | a   | 5   | 20  | b   | 5   |
[__T1.A (integer)__, Q (char), R (integer), T2.A (integer), B (char), C (integer)]

## Part 2 - Chess Queries

Consider the schemas below for a Chess database, similar to the one you designed in Homework 2, except some columns are left out for simplicity. Note that the instance data given is just to show an example, and it may change (you can't hard-code your queries based on these instances).

### Events

|Name|Year|eID|
|---|---|---|
|World Championship|1987|1|
|Moscow Open|2018|2|
|World Championship|2018|3|

### Players

|Name|Elo|pID|
|---|---|---|
|Magnus Carlsen|2882|1|
|Judit Polgar|2735|2|
|Fabiano Caruana|2844|3|
|Gary Kasparov|2851|4|
|Anatoly Karpov|2780|5|

### Games: wpID (white) and bpID (black) reference pID from Players

|gID|eID|Result|wpID|bpID|
|---|---|---|---|---|
|1|3|1/2-1/2|1|3|
|2|3|1/2-1/2|3|1|
|3|2|1-0|2|1|
|4|1|1/2-1/2|4|5|
|5|3|0-1|3|1|

Recall that in this database, a result of "1-0" means white won, "0-1" means black won, and "1/2-1/2" means it was a draw.

Hint: you can't natural join Games with Players because they have no columns with the same name.

Write relational algebra queries for the following. You can (and should) write your query on multiple lines if you use the renaming operator.

1. Find the names of any player with an Elo rating of 2850 or higher.
	1. $\rho$(HighEloPlayers, $\sigma$<sub>Elo>=2850</sub>(Players))
	2. $\rho$(HighEloNames, $\pi$<sub>Name</sub>(HighEloPlayers))
2. Find the names of any player who has ever played a game as white.
	1. $\rho(FullTable, Players \bowtie_{wpID=pID} Games)$
	2. $\rho$(PlayedWhite, $\pi$<sub>Name</sub>(FullTable))
3. Find the names of any player who has ever won a game as white.
	1. $\rho(FullTable, Players \bowtie_{wpID=pID} Games)$
	2. $\rho(WonWhiteRows, \sigma_{Result=1-0}(FullTable))$
	3. $\rho$(WonWhite, $\pi$<sub>Name</sub>(PlayedWhiteRows))
4. Find the names of any player who played any games in 2018.
	1. $\rho(PlayerstoGames, Players \bowtie_{wpID=pID\vee bpID=pID} Games)$
	2. $\rho$(FullTable, PlayerstoGames$\bowtie$Events)
	3. $\rho$(Played2018, $\sigma$<sub>Year=2018</sub>(FullTable))
	4. $\rho$(Played2018Names, $\pi$<sub>Name</sub>(Played2018))
5. Find the names and dates of any event in which Magnus Carlsen lost a game.
	1. $\rho$(Magnus, Players$\sigma$<sub>Name=Magnus Carlsen</sub>(Players))
	2. $\rho(MagnusTable, Magnus \bowtie_{pID=wpID\vee pID=bpID} Games)$
	3. $\rho(MagnusWhiteLoss, \sigma_{pID=wpID\wedge Result=0-1}(MagnusTables))$
	4. $\rho(MagnusBlackLoss, \sigma_{pID=bpID\wedge Result=1-0}(MagnusTables))$
	5. $\rho$(MagnusLosses, MagnusWhiteLoss $\cup$ MagnusBlackLoss)
	6. $\rho$(MagnusLossEvents, MagnusLosses$\bowtie$Events)
	7. $\rho$(MagnusLossInfo, $\pi$<sub>Name, Year</sub>(MagnusLossEvents))
6. Find the names of all opponents of Magnus Carlsen. An opponent is someone who he has played a game against. Hint: Both Magnus and his opponents could play as white or black.
	1. $\rho$(Magnus, Players$\sigma$<sub>Name=Magnus Carlsen</sub>(Players))
	2. $\rho(MagnusTable, Magnus \bowtie_{pID=wpID\vee pID=bpID} Games)$
	3. $\rho$(OppBlack, $\sigma$<sub>pID!=wpID</sub>(MagnusTables))
	4. $\rho$(OppWhite, $\sigma$<sub>pID!=bpID</sub>(MagnusTables))
	5. $\rho(Opponents, Players\pi_{Name}(OppWhite\cup OppBlack))$

## Part 3 - LMS Queries

Consider [this basic database for a learning management system](https://github.com/msd2026/CS6016/blob/main/labs/students.pdf).

### Part 3.1:

a) Provide the relation that is the result of the following query. Your relation should be in the form of a table, and should include the schema.

$\rho(C, \pi_{sid}(\sigma_{Grd=C}(Enrolled)))$

$π N a m e ( ( π s i d ( E n r o l l e d ) − C ) ⋈ S t u d e n t s )$

| Name     |
| -------- |
| Hermione |
| Harry    |
[Name]

b) Provide a simple English description of what the query is searching for. Your description should be in general terms (remember that the original LMS instance data may change).

This is looking for the Names of every student that doesn't have a C on their report cards.
### Part 3.2:

a) Provide the relation that is the result of the following query. Your relation should be in the form of a table, and should include the schema.

$\rho(S1, Students)$

$\rho(S2, Students)$

$\pi_{S2.Name}(\sigma_{S1.Name == Ron \wedge S1.DOB == S2.DOB \wedge S2.name != Ron}(S1 \times S2))$


| Name     |
| -------- |
| Hermione |
[Name]

b) Provide a simple English description of what the query is searching for. Your description should be in general terms (remember that the original LMS instance data may change).

This is asking for a table for the name of all students in every possible combination of tables S1 and S2 where the student's name is Ron in S1, has a matching DOB between S1 and S2, and the student's name is not Ron is S2.

### Part 3.3:

a) Provide the relation that is the result of the following query. Your relation should be in the form of a table, and should include the schema.

$\pi_{Name}((\pi_{cid, sid} (Enrolled)/\pi_{sid}(Students)) \bowtie Courses)$

| Name |
| ---- |
[Name]

b) Provide a simple English description of what the query is searching for. Your description should be in general terms (remember that the original LMS instance data may change).

This is looking for the name of every student for any course where every student is enrolled. However, there are none with the data provided.
## Part 4

Provide a relational algebra query that uses the divide operator to find the names of all students who are taking all of the 3xxx-level classes.
$\rho(3xxxCourses, pi_{cid}(\sigma_{cid>=3000\wedge cid<4000}(Courses)))$
$\pi_{Name}(\pi_{cid, sid}(Enrolled)/3xxxCourses) \bowtie Students)$

Commit your .md or .pdf file to your git repo (It's probably a good idea to include your .tex or .docx file as well, but we'll only look at the PDF).