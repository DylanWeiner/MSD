// top level code is stuck inside of an implied
// public static void main inside of an implied class ( I think)

using Npgsql;

// The best part of C++ is  }.  Destructors are awesome!
//No destructor in C#, but we have an interface: IDisposable { void dispose(); }, close enough
/*
 *
 * try {
 *  var mydisposable = new ...();
 *  ... whatever
 * } finally {
 *   mydispoable.dispose();
 * }
 */

/* shorthand
    using(var mydisposable = new ...()){
    
    }
 */

// short, short version: using var mydisposable = new ...

if(false){
    Console.Out.WriteLine("Enter your card num");
    String cardnum = Console.In.ReadLine();


    await using var dataSource = 
        NpgsqlDataSource.Create("Host=atr.eng.utah.edu;Username=bejones;Database=library");
    await using var command = 
        dataSource.CreateCommand("SELECT name, cardnum FROM patrons WHERE cardnum = " + cardnum);
    await using var reader = await command.ExecuteReaderAsync();

    while (await reader.ReadAsync())
    {
        Console.WriteLine(reader.GetString(0) + " " + reader.GetInt32(1));
    }
}

{
    await using var dataSource = 
        NpgsqlDataSource.Create("Host=atr.eng.utah.edu;Username=bejones;Database=bejones");
    await using var command = 
        dataSource.CreateCommand("CREATE TABLE demotable( column varchar(20) PRIMARY KEY)");
   
    var rowsAffected = await command.ExecuteNonQueryAsync();
    Console.WriteLine("rows updated: " + rowsAffected );

    for (int i = 0; i < 2; i++)
    {
        await using var insertCommand = dataSource.CreateCommand("INSERT INTO demotable VALUES('hello')");
        Console.WriteLine("rows affected " + await insertCommand.ExecuteNonQueryAsync());
    }
    

}

if (true)
{
    await using var dataSource =
    NpgsqlDataSource.Create("Host=atr.eng.utah.edu;Username=u1150859;Database=library");
    await using var command = dataSource.CreateCommand("SELECT name, cardnum FROM patrons WHERE name = $1");
    command.Parameters.Add(new NpgsqlDataSource { Value = name });

    await using car reader = await cmd.ExecuteReaderAsync();

    while (await reader.ReadAsync())
    {
        Console.WriteLine(reader.GetString(0) + " " + reader.GetInt32(1));
    }
} // This is to avoid injection attacks.

/// <summary>
/// this is a class that does nothing
/// </summary>
class SomeClass
{

    /// <summary>
    /// 
    /// </summary>
    /// <param name="s"></param>
    /// <returns></returns>
    public int someMethod(String s)
    {
        return s.Length;
    }

}