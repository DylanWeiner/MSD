You've been asked to design a data base for a fantasy ultimate frisbee league.

- Ultimate frisbee is a sport (game, whatever) played between 2 teams
- Each team has many players (7 on the field at a time, but ~20 will play at some point during the game), and a player will only play on 1 team
- A game score is just the number of goals scored by each team (15-11 for example)
- A player's fantasy score depends on 4 stats, and we should track each of these for each player in a game:
    - score a goal
    - assist (throw) a goal
    - get a block (play good defense)
    - cause a turnover (bad, they get -1 point)
- A player also gets 1 bonus point if their team wins the game
- A fantasy team is a group of players which can be on many different ultimate frisbee teams
- Fantasy points are accumulated over all games during a given week
- We'll need to be able to compute winners of "fantasy matchups" by adding up the score of all players of 2 fantasy teams over the course of a week, and seeing which one had more points

Design a DB schema for this application using the following approach:

- Determine the total set of all attributes you'll need to track
- Determine the total set of functional dependencies your DB must enforce

Starting with a "monster table" follow the general BCNF decomposition algorithm to normalize the DB so that the functional dependencies will be enforced (you don't have to follow the algorithm precisely)

Once you have your design completed, commit it to your github repo.

Additionally, write up a short discussion about how this design approach compares to the ER diagram approach we discussed earlier in the semester. What worked better with one method compared to the other? How might you combine both approaches to find the best middle ground?

The schema design part can be done with a partner, but each partner should submit their own design. The discussion should be done individually

| Team1Name | Team2Name | Player | GameID | Goals | Assists | Blocks | Turnovers | Victorious | Week | Team1ID | Team1Score | Team2ID | Team2Score | FantasyTeamID |
| --------- | --------- | ------ | ------ | ----- | ------- | ------ | --------- | ---------- | ---- | ------- | ---------- | ------- | ---------- | ------------- |
|           |           |        |        |       |         |        |           |            |      |         |            |         |            |               |
FDs: Player->Team1Name, Team1ID; GameID->Team1ID, Team1Name, Team2ID, Team2Name, Team1Score, Team2Score, Week; Player -> FantasyTeamID; Team2ID->Team2Name; Team1ID->Team1Name

ER Diagrams have fairly large upfront cognitive loads because you need to start out by exercisin critical thinking on how everything should be split up. Then it gets progessively more intuitive the further into the process you go. However, this approach is a fairly simple initial approach; you can take all the values you'll need to record, shave off anything that seems unnecessary, and then the cognitive load will increase as you try to decide what the optimal division and necessary constraints should look like. I think the ER Diagram feels more intuitive for me because I naturally look for the division of the data and start with a preconception of how I'd like to build my relational db. However, I can see the appeal of this approach, especially with larger sets of attributes. I think starting with the super table to visualize all of our attributes and then drawing out a rough ER diagram to conceptualize the schema feels the most intuitive for combining the two approached.

Teams[TeamID, Player]
TeamRegister[TeamID, TeamName]
PlayerScores[Player, Goals, Assists, Blocks, Turnovers, Victorious]
GameMatchup[GameID, Team1ID, Team2ID]
FantasyRosters[FantasyTeamID, Player]


