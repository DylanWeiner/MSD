Intermediate Retrieval Queries
    Find the Titles of the library's oldest `<N>` books. Assume the lowest serial number is the oldest book.
	    SELECT title FROM titles NATURAL JOIN inventory WHERE serial = (SELECT MIN(serial) FROM inventory);
    Find the name of the person who has checked out the most recent book. Assume the highest serial number is the newest book. Hint: the highest serial number book may not be checked out by anyone.
	    SELECT name FROM patrons NATURAL JOIN checkedout NATURAL JOIN inventory WHERE serial = (SELECT MAX(serial) FROM inventory);
    Find the phone number(s) of anyone who has not checked out any books.
	    SELECT phone FROM phones NATURAL JOIN patrons WHERE cardnum NOT IN (SELECT cardnum FROM checkedout);
    The library wants to expand the number of unique selections in its inventory, thus, it must know the ISBN and Title of all books that it owns at least one copy of. Create a query that will return the ISBN and Title of every book in the library, but will not return the same book twice.
	    SELECT DISTINCT isbn, title FROM titles NATURAL JOIN inventory;

Advanced Retrieval Queries
    Find the name of the patron who has checked out the most books.
	    WITH CheckedPatrons AS(SELECT name, COUNT(cardnum) FROM patrons NATURAL JOIN checkedout GROUP BY cardnum)
	    SELECT name FROM CheckedPatrons WHERE count=(SELECT MAX(count) FROM CheckedPatrons);
    Find the Authors who have written more than one book. Assume that two Authors with the same name are the same Author for this query.
	    WITH AuthorsBooks AS(SELECT author, COUNT(author) FROM titles GROUP BY author)
	    SELECT author FROM (SELECT author, count FROM AuthorsBooks WHERE count >= 2);
    Find the Authors for which the library has more than one book in inventory (this includes multiple copies of the same book). Assume that two Authors with the same name are the same Author for this query.
	    WITH MultBooks AS(SELECT author, COUNT(author) FROM titles NATURAL JOIN inventory GROUP BY author)
	    SELECT author FROM (SELECT author, count FROM MultBooks WHERE count >= 2);

A couple more queries
	The library wants to implement a customer loyalty program based on how many books each patron has checked out. Provide an SQL query that returns the names, number of books they have checked out, and loyalty level of each Patron. The loyalty level should be the string "Platinum" if they have checked out > 2 books, "Gold" if they have 2 books, "Silver" if they have 1 book, and "Bronze" if they have no books. Hint: remember that NULL represents an unknown in SQL (it does not represent 0).
		SELECT name, count, CASE
			WHEN count > 2 THEN 'Platinum'
			WHEN count = 2 THEN 'Gold'
			WHEN count = 1 THEN 'Silver'
			ELSE 'Bronze'
		END FROM (SELECT name, COUNT(name) FROM checkedout NATURAL JOIN patrons GROUP BY name);
	Find the name of the oldest book by each author. By oldest, we mean the book with the smallest serial number.
		WITH AllBooks AS (SELECT MIN(serial) FROM titles NATURAL JOIN inventory GROUP BY author)
		SELECT title FROM AllBooks JOIN inventory ON AllBooks.min = inventory.serial NATURAL JOIN titles;