A database contains the following table for former-employee records:

| eID (int) (primary key) | Start Date (Date) | End Date (Date) |
| ----------------------- | ----------------- | --------------- |

There are two common queries performed on this table:
	Find all employees that started after a certain date
		CREATE INDEX on Employees(StartDate);
	Find all employees that started on a certain date, and worked until at least another certain date
		CREATE INDEX on Employees(StartDate);
		CREATE INDEX on Employees(EndDate);


A database contains the following table for tracking student grades in classes

| studentID (int) (primary key) | className (varchar(10)) (primary key) | Grade (char(1)) |
| ----------------------------- | ------------------------------------- | --------------- |

This table only tracks whole letter grades, no '-' or '+' modifiers. Assume many students can take a class, and a student can take many classes. The primary key is created in the order (studentID, className). The common queries performed on this table are:
	Get all students with a grade better than 'B'
	Get all classes where any student earned a grade worse than 'D'
		CREATE INDEX on Students(Grade, className);

Using the same grade database, but now the common queries are:
	Get all classes ordered by class name
	Get all students who earned an 'A' in a certain class
		CREATE INDEX on Students(className, Grade);

Queries on the chess database
Assume the only existing indexes are the primary index on each table (despite whatever indexes are on the actual tables. Remember, we're assuming there are NOT indexes created for foreign key constraints). Also assume the actual table sizes in the Chess database (ie big).
Common queries are:
	`select Name from Players where Elo >=2050;`
		CREATE INDEX on Players(Elo);
	`select Name, gID from Players join Games where pID=WhitePlayer;`
		CREATE INDEX on Games(WhitePlayer);

Queries on the public Library database

Assume the only existing indexes are the primary index on each table (despite whatever indexes are on the actual tables).

Common queries are:

`select * from Inventory natural join CheckedOut;`
	CREATE INDEX on CheckedOut(serial)
		This is likely automatically created due to fk.

More library queries:
Assume the only existing indexes are the primary index on each table (despite whatever indexes are on the actual tables).
Common queries are:
	`select * from Inventory natural join CheckedOut where CardNum=2;`
	`select * from Patrons natural join CheckedOut;`
		CREATE INDEX on CheckedOut(cardnum);
Still more library queries
Assume the only existing indexes are the primary index on each table (despite whatever indexes are on the actual tables). Also assume that the Library has been auto-scaffolded as discussed in class.
```
 var query = db.Titles.Select( t => new {
 	title = t.Title,
 	Serials = t.SerialNavigation.toList()
 });
```
CREATE INDEX on Inventory(Isbn);

Students table:
Consider the students table from #2 in Part 1 above. Assume that an int occupies 4 bytes, and a varchar(10) occupies 10 bytes.

How many rows of the table can be placed into the first leaf node of the primary index before it will split?
    273
What is the maximum number of keys stored in an internal node of the primary index? (Remember to ignore pointer space. Remember that internal nodes have a different structure than leaf nodes.)
    292 keys
What is the maximum number of rows in the table if the primary index has a height of 1? (A tree of height 1 has 2 levels)
    293 * 273 = 79,989 rows
What is the minimum number of rows in the table if the primary index has a height of 1? (A tree of height 1 has 2 levels)
    2 * 137 = 274 rows
If there is a secondary index on Grade, what is the maximum number of entries a leaf node can hold in the secondary index?
    4096 entries

Another table
Assume that for some table, rows occupy 128 bytes.

What is the maximum number of leaf nodes in the primary index if the table contains 48 rows?
    3 leaf nodes
What is the minimum number of leaf nodes in the primary index if the table contains 48 rows?
	2 leaf nodes