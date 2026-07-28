using System.Diagnostics;
using System.Text.RegularExpressions;
using Microsoft.AspNetCore.Components.Forms;
using Npgsql;

namespace ChessBrowser.Components.Pages;

public struct GameMetadata
{
    public string EventName {get; set;}
    public string Site {get; set;}
    public DateTime Date {get; set;}
    public float Round {get; set;}
    public string WhitePlayer {get; set;}
    public string BlackPlayer {get; set;}
    public string ResultData {get; set;}
    public int WhiteElo {get; set;}
    public int BlackElo {get; set;}
    public string Eco {get; set;}
    public DateTime EventDate {get; set;}
}

public struct MoveList
{
    public string Moves  {get; set;}
}

public partial class ChessBrowser
{
  /// <summary>
  ///     Bound to the Database form input
  /// </summary>
  private string Database = "";

  /// <summary>
  ///     Represents the progress percentage of the current
  ///     upload operation. Update this value to update
  ///     the progress bar.
  /// </summary>
  private int Progress;

  /// <summary>
  ///     Bound to the Username form input
  /// </summary>
  private string Username = "";

  /// <summary>
  ///     This method runs when a PGN file is selected for upload.
  ///     Given a list of lines from the selected file, parses the
  ///     PGN data, and uploads each chess game to the user's database.
  /// </summary>
  /// <param name="PGNFileLines">The lines from the selected file</param>

  private string[] GetRegexChunks(string[] PGNFileLines)
  {
      string fullText = string.Join(Environment.NewLine, PGNFileLines);
            
      string[] chunks = Regex.Split(fullText, @"(\n){2,}")
          .Select(c => c.Trim())
          .Where(c => !string.IsNullOrEmpty(c))
          .ToArray();
      
      return chunks;
  }

  private GameMetadata ParseMetadata(string chunk)
  {
      GameMetadata metadata = new GameMetadata();
      
      var tagRegex = new Regex(@"\[(\w+)\s+""([^""]+)""\]"); // Looks for value inside "" and key right after the [ 
      MatchCollection matches = tagRegex.Matches(chunk);
      
      foreach (Match match in matches)
      {
          var key = match.Groups[1].Value;
          var val = match.Groups[2].Value;

          switch (key)
          {
              case "Event": metadata.EventName = val; break;
              case "Site":  metadata.Site = val; break;
              case "Date":
                  if (DateTime.TryParse(val, out DateTime parsedDate))
                  {
                      metadata.Date = parsedDate; 
                  }
                  else
                  {
                      metadata.Date = DateTime.MinValue;
                  } break;
              case "Round": metadata.Round = float.Parse(val); break;
              case "White": metadata.WhitePlayer = val; break;
              case "Black": metadata.BlackPlayer = val; break;
              case "Result": metadata.ResultData = val; break;
              case "WhiteElo": metadata.WhiteElo = int.Parse(val); break;
              case "BlackElo": metadata.BlackElo = int.Parse(val); break;
              case "ECO": metadata.Eco = val; break;
              case "EventDate": if (DateTime.TryParse(val, out DateTime parsedEventDate))
                  {
                      metadata.EventDate = parsedEventDate; 
                  }
                  else
                  {
                      metadata.EventDate = DateTime.MinValue;
                  } break;; break;
          }
      }
      return metadata;
  }

  private MoveList ParseMoveList(string chunk)
  {
      MoveList moveList = new MoveList();
      moveList.Moves = chunk;
      return moveList;
  }
  
  private async Task InsertGameData(string[] PGNFileLines)
    {
        Progress = 0;
        int round = 0;
        // This will build a connection string to your user's database on atr,
        // assuming you've filled in the credentials in the GUI

        var connectionString = GetConnectionString();

        //wrap this in try/catch with exception printing so the UI doesn't swallow your exceptions
        try
        {
            var gameInfo = new Dictionary<GameMetadata, MoveList>();
            
            string[] chunks = GetRegexChunks(PGNFileLines);
            for (int i = 1; i < chunks.Length; i+=2)
            {
                gameInfo[ParseMetadata(chunks[i-1])] = ParseMoveList(chunks[i]);
            }
            
            // Open a connection
            await using var dataSource = 
                NpgsqlDataSource.Create("Host=atr.eng.utah.edu;Username=u1150859;Database=u1150859");
            await using var CreateEvents =
            dataSource.CreateCommand("""
                                      CREATE TABLE IF NOT EXISTS events (
                                         name VARCHAR(255) NOT NULL,
                                         site VARCHAR(255) NOT NULL,
                                         date DATE NOT NULL,
                                         eid SERIAL PRIMARY KEY NOT NULL,
                                         UNIQUE (name, site, date)
                                      );
                                     """);
            
            await using var CreatePlayers =
                dataSource.CreateCommand("""
                                           CREATE TABLE IF NOT EXISTS players (
                                               name VARCHAR(255) UNIQUE NOT NULL,
                                               elo INTEGER,
                                               pid SERIAL PRIMARY KEY NOT NULL
                                             );
                                         """);
            await using var CreateGames =
                dataSource.CreateCommand("""
                                         CREATE TABLE IF NOT EXISTS games (
                                             round VARCHAR(10) NOT NULL,
                                             result VARCHAR(1) NOT NULL,
                                             moves VARCHAR(2000) NOT NULL,
                                             blackplayer INTEGER REFERENCES players(pid) NOT NULL,
                                             whiteplayer INTEGER REFERENCES players(pid) NOT NULL,
                                             gamedate DATE NOT NULL,
                                             eid INTEGER REFERENCES events(eid) NOT NULL,
                                             PRIMARY KEY (round, blackplayer, whiteplayer, eid));
                                         """);
            var playersTable = await CreatePlayers.ExecuteNonQueryAsync();
            Console.WriteLine("Table Created: " + playersTable );
            
            var eventsTable = await CreateEvents.ExecuteNonQueryAsync();
            Console.WriteLine("Table Created: " + eventsTable );
            
            var gamesTable = await CreateGames.ExecuteNonQueryAsync();
            Console.WriteLine("Table Created: " + gamesTable );
            
             foreach (var game in gameInfo)
             {
                 await using var InsertWhitePlayer =
                     dataSource.CreateCommand("""
                                                INSERT INTO players (name, elo) VALUES ($1, $2)
                                                ON CONFLICT (name)
                                                DO UPDATE SET elo = $2;
                                              """);
                 InsertWhitePlayer.Parameters.Add(new NpgsqlParameter{Value = game.Key.WhitePlayer});
                 InsertWhitePlayer.Parameters.Add(new NpgsqlParameter{Value = game.Key.WhiteElo});
                 
                 await using var wPlayers = await InsertWhitePlayer.ExecuteReaderAsync();

                 while (await wPlayers.ReadAsync())
                 {
                     Console.WriteLine(wPlayers.GetString(0) + " " + wPlayers.GetInt32(1));
                 }
                 
                 await using var InsertBlackPlayer =
                     dataSource.CreateCommand("""
                                                INSERT INTO players (name, elo) VALUES ($1, $2)
                                                ON CONFLICT (name)
                                                DO UPDATE SET elo = $2;
                                              """);
                 InsertBlackPlayer.Parameters.Add(new NpgsqlParameter{Value = game.Key.BlackPlayer});
                 InsertBlackPlayer.Parameters.Add(new NpgsqlParameter{Value = game.Key.BlackElo});
                
                 await using var bPlayers = await InsertBlackPlayer.ExecuteReaderAsync();
                 
                 while (await bPlayers.ReadAsync())
                 {
                     Console.WriteLine(bPlayers.GetString(0) + " " + bPlayers.GetInt32(1));
                 }
                 
                await using var InsertEvent =
                    dataSource.CreateCommand("""
                                               INSERT INTO events (name, site, date) VALUES ($1, $2, $3)
                                               ON CONFLICT (name, date, site) DO NOTHING;
                                             """);
                InsertEvent.Parameters.Add(new NpgsqlParameter{Value = game.Key.EventName});
                InsertEvent.Parameters.Add(new NpgsqlParameter{Value = game.Key.Site});
                InsertEvent.Parameters.Add(new NpgsqlParameter{Value = game.Key.EventDate});
                
                await using var newEvents = await InsertEvent.ExecuteReaderAsync();
                
                while (await newEvents.ReadAsync())
                {
                    Console.WriteLine(newEvents.GetString(0) + " " + newEvents.GetInt32(1));
                }
                
                await using var getBlackPlayer =
                    dataSource.CreateCommand("""
                                             SELECT pid FROM players WHERE name = $1;
                                             """);
                getBlackPlayer.Parameters.Add(new NpgsqlParameter{Value = game.Key.BlackPlayer});
                 int blackid = (int)await getBlackPlayer.ExecuteScalarAsync();
                 
                await using var getWhitePlayer =
                    dataSource.CreateCommand("""
                                             SELECT pid FROM players WHERE name = $1;
                                             """);
                getWhitePlayer.Parameters.Add(new NpgsqlParameter{Value = game.Key.WhitePlayer});
                int whiteid = (int)await getWhitePlayer.ExecuteScalarAsync();

                char result = ' ';
                if (game.Key.ResultData == "1/2-1/2")
                {
                    result = 'D';
                }
                else if (game.Key.ResultData == "1-0")
                {
                    result = 'W';
                }
                else if (game.Key.ResultData == "0-1")
                {
                    result = 'B';
                }
                
                await using var getEventId =
                    dataSource.CreateCommand("""
                                             SELECT eid FROM events WHERE name = $1;
                                             """);
                getEventId.Parameters.Add(new NpgsqlParameter{Value = game.Key.EventName});
                int eventid = (int)await getEventId.ExecuteScalarAsync();
                
                await using var InsertGame =
                    dataSource.CreateCommand("""
                                               INSERT INTO games (round, result, blackplayer, whiteplayer, moves, eid, gamedate) VALUES ($1, $2, $3, $4, $5, $6, $7)
                                               ON CONFLICT (round, blackplayer, whiteplayer, eid) DO NOTHING;
                                             """);
                
                
                InsertGame.Parameters.Add(new NpgsqlParameter{Value = game.Key.Round});
                InsertGame.Parameters.Add(new NpgsqlParameter{Value = result});
                InsertGame.Parameters.Add(new NpgsqlParameter{Value = blackid});
                InsertGame.Parameters.Add(new NpgsqlParameter{Value = whiteid});
                InsertGame.Parameters.Add(new NpgsqlParameter{Value = game.Value.Moves});
                InsertGame.Parameters.Add(new NpgsqlParameter{Value = eventid});
                InsertGame.Parameters.Add(new NpgsqlParameter{Value = game.Key.Date});
                
                await using var newGames = await InsertGame.ExecuteReaderAsync();
                
                while (await newGames.ReadAsync())
                {
                }
                
                round += 1;
                float iter = (round / ((float)chunks.Length / 2)) * 100;
                
                Progress = (int)iter;

                // This tells the GUI to redraw after you update Progress (this should go inside your loop)
                await InvokeAsync(StateHasChanged);
            }
            
                                                       // TODO:
                                                       //   Update the Progress member variable every time progress has been made
                                                       //   (e.g. one iteration of your upload loop)
                                                       //   This will update the progress bar in the GUI
                                                       //   Its value should be an integer representing a percentage of completion
                                                       
                                                   }
                                                   catch (Exception e)
                                                   {
                                                       Console.WriteLine("uh oh: " + e.Message + e.StackTrace);
                                                       throw;
                                                   }
                                               }


                                             /// <summary>
                                             ///     Queries the database for games that match all the given filters.
                                             ///     The filters are taken from the various controls in the GUI.
                                             /// </summary>
                                             /// <param name="white">The white player, or "" if none</param>
                                             /// <param name="black">The black player, or "" if none</param>
                                             /// <param name="opening">The first move, e.g. "1.e4", or "" if none</param>
                                             /// <param name="winner">The winner as "W", "B", "D", or "" if none</param>
                                             /// <param name="useDate">true if the filter includes a date range, false otherwise</param>
                                             /// <param name="start">The start of the date range</param>
                                             /// <param name="end">The end of the date range</param>
                                             /// <param name="showMoves">true if the returned data should include the PGN moves</param>
                                             /// <returns>A string separated by newlines containing the filtered games</returns>
                                             private async Task<string> PerformQuery(string white, string black, string opening,
                                                   string winner, bool useDate, DateTime start, DateTime end, bool showMoves)
                                               {
                                                   // This will build a connection string to your user's database on atr,
                                                   // assuming you've typed a user and password in the GUI
                                                   var connection = GetConnectionString();

                                                   // Build up this string containing the results from your query
                                                   var parsedResult = "";

                                                   // Use this to count the number of rows returned by your query
                                                   // (see below return statement)
                                                   var numRows = 0;

                                                   await using var conn = NpgsqlDataSource.Create(connection);

                                                   try
                                                   {
                                                       string sqlQuery = """
                                                                         SELECT
                                                                         events.name eventname, site, gamedate, p1.name wname, p2.name bname, p1.elo welo, p2.elo belo, result, moves
                                                                         FROM events
                                                                         NATURAL JOIN games
                                                                         JOIN players p1 ON games.whiteplayer=p1.pid
                                                                         JOIN players p2 ON games.blackplayer=p2.pid
                                                                         WHERE 1=1
                                                                         """;
                                                       
                                                       var parameters = new List<NpgsqlParameter>();
                                                       
                                                       if (!string.IsNullOrWhiteSpace(white))
                                                       {
                                                           sqlQuery += " AND p1.name = @whiteP";
                                                           parameters.Add(new NpgsqlParameter("@whiteP", NpgsqlTypes.NpgsqlDbType.Varchar) { Value = white });
                                                       }
                                                       
                                                       if (!string.IsNullOrWhiteSpace(black))
                                                       {
                                                           sqlQuery += " AND p2.name = @blackP";
                                                           parameters.Add(new NpgsqlParameter("@blackP", NpgsqlTypes.NpgsqlDbType.Varchar) { Value = black });
                                                       }

                                                       if (!string.IsNullOrWhiteSpace(winner))
                                                       {
                                                           sqlQuery += " AND result = @gameResult";
                                                           parameters.Add(new NpgsqlParameter("@gameResult", NpgsqlTypes.NpgsqlDbType.Char) { Value = winner[0] });
                                                       }
                                                       
                                                       if (!string.IsNullOrWhiteSpace(opening))
                                                       {
                                                           sqlQuery += " AND moves LIKE @openingMove";
                                                           parameters.Add(new NpgsqlParameter("@openingMove", NpgsqlTypes.NpgsqlDbType.Varchar) { Value = $"1.{opening}%" });
                                                       }

                                                       if (useDate)
                                                       {
                                                           sqlQuery += " AND gamedate >= @startDate AND gamedate <= @endDate";
                                                           parameters.Add(new NpgsqlParameter("@startDate", NpgsqlTypes.NpgsqlDbType.Date) { Value = start });
                                                           parameters.Add(new NpgsqlParameter("@endDate", NpgsqlTypes.NpgsqlDbType.Date) { Value = end });
                                                       }

                                                       sqlQuery += ";";
                                                       
                                                       Console.WriteLine(sqlQuery);
                                                       
                                                       await using var FindGame =
                                                           conn.CreateCommand(sqlQuery);
                                                       
                                                       FindGame.Parameters.AddRange(parameters.ToArray());
                                                       
                                                       await using var findReader = await FindGame.ExecuteReaderAsync();
                                                       
                                                       string eventName = "";
                                                       string siteName = "";
                                                       DateTime gameDate = new DateTime();
                                                       int whiteElo = 0;
                                                       int blackElo = 0;
                                                       string whitePlayer = "";
                                                       string blackPlayer = "";
                                                       char gameRes = ' ';
                                                       string movesPlayed = "";
                                                       while (await findReader.ReadAsync())
                                                       {
                                                           eventName = findReader["eventname"].ToString();
                                                           siteName = findReader["site"].ToString();
                                                           gameDate = Convert.ToDateTime(findReader["gamedate"].ToString());
                                                           whiteElo = Convert.ToInt32(findReader["welo"].ToString());
                                                           blackElo = Convert.ToInt32(findReader["belo"].ToString());
                                                           whitePlayer = findReader["wname"].ToString();
                                                           blackPlayer = findReader["bname"].ToString();
                                                           gameRes = findReader.GetChar(7);
                                                           movesPlayed = findReader["moves"].ToString();
                                                           numRows++;
                                                           
                                                           parsedResult += $"""
                                                                            Event: {eventName}
                                                                            Site: {siteName}
                                                                            Date: {gameDate}
                                                                            White: {whitePlayer} ({whiteElo})
                                                                            Black: {blackPlayer} ({blackElo})
                                                                            Result: {gameRes}
                                                                            
                                                                            """;
                                                           if (showMoves)
                                                           {
                                                               parsedResult += $"Moves: {movesPlayed}\n\n";
                                                           }
                                                           else
                                                           {
                                                               parsedResult += "\n";
                                                           }
                                                       }
                                                   }
                                                   catch (Exception e)
                                                   {
                                                       Console.WriteLine(e.Message);
                                                       Console.WriteLine(e.StackTrace);
                                                   }
                                                   
                                                   return numRows + " results\n" + parsedResult;
                                               }


                                               private string GetConnectionString()
                                               {
                                                   //If you install postgres with homebrew, you can use this
                                                   //connection string (with modifications) to connect to it instead of ATR
                                                   //return "Server=localhost; Username=u1150859; database=chess";
                                                   return "server=atr.eng.utah.edu;database=" + Database + ";Username=" + Username;
                                               }


                                               /// <summary>
                                               ///     This method will run when the file chooser is used.
                                               ///     It loads the files contents as an array of strings,
                                               ///     then invokes the InsertGameData method.
                                               /// </summary>
                                               /// <param name="args">The event arguments, which contains the selected file name</param>
                                               private async void HandleFileChooser(EventArgs args)
                                               {
                                                   try
                                                   {
                                                       var fileContent = string.Empty;

                                                       var eventArgs = args as InputFileChangeEventArgs ?? throw new Exception("unable to get file name");
                                                       if (eventArgs.FileCount == 1)
                                                       {
                                                           var file = eventArgs.File;
                                                           if (file is null) return;

                                                           // load the chosen file and split it into an array of strings, one per line
                                                           using var stream = file.OpenReadStream(1000000); // max 1MB
                                                           Console.WriteLine("Starting to read file");
                                                           using var reader = new StreamReader(stream);
                                                           fileContent = await reader.ReadToEndAsync();
                                                           var fileLines = fileContent.Split(new[] { "\r\n", "\r", "\n" }, StringSplitOptions.None);

                                                           // insert the games, and don't wait for it to finish
                                                           // _ = throws away the task result, since we aren't waiting for it
                                                           Console.WriteLine("Read file, about to process it");
                                                           _ = InsertGameData(fileLines);
                                                       }
                                                   }
                                                   catch (Exception e)
                                                   {
                                                       Debug.WriteLine("an error occurred while loading the file..." + e);
                                                   }
                                               }
                                           }