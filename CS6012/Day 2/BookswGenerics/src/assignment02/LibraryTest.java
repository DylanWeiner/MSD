package assignment02;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @Test
    public void testEmpty() {
        Library<String> lib = new Library<>();
        assertNull(lib.lookup(978037429279L));

        ArrayList<LibraryBook<String>> booksCheckedOut = lib.lookup("Jane Doe");
        assertEquals(booksCheckedOut.size(), 0);

        assertFalse(lib.checkout(978037429279L, "Jane Doe", 1, 1, 2008));
        assertFalse(lib.checkin(978037429279L));
        assertFalse(lib.checkin("Jane Doe"));
    }

    @Test
    public void testNonEmpty() {

        var lib = new Library<String>();
        // test a small library
        lib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib.add(9780446580342L, "David Baldacci", "Simple Genius");

        assertNull(lib.lookup(9780330351690L)); //not checked out
        var res = lib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008);
        assertTrue(res);
        var booksCheckedOut = lib.lookup("Jane Doe");
        assertEquals(booksCheckedOut.size(), 1);
        assertEquals(booksCheckedOut.get(0), new Book(9780330351690L, "Jon Krakauer", "Into the Wild"));
        assertEquals(booksCheckedOut.get(0).getHolder(), "Jane Doe");
        assertEquals(booksCheckedOut.get(0).getDueDate(), new GregorianCalendar(2008, 1, 1));
        res = lib.checkin(9780330351690L);
        assertTrue(res);
        res = lib.checkin("Jane Doe");
        assertFalse(res);
    }

    @Test
    public void testLargeLibrary() {
        // test a medium library
        var lib = new Library<String>();
        lib.addAll("src/assignment02/Mushroom_Publishing.txt");


        var ex1 = lib.checkout(9781843190028L, "Sybil Eye", 11, 19, 2025);
        var ex2 = lib.checkout(9781843190042L, "Sybil Eye", 11, 19, 2025);
        var ex3 = lib.checkout(9781843190073L, "Sybil Eye", 11, 19, 2025);
        var ex4 = lib.checkout(9781843190110L, "Sybil Eye", 11, 19, 2025);

        ArrayList<LibraryBook<String>> ex = new ArrayList<LibraryBook<String>>();
        for (int i = 0; i < 4; i++) {
            ex.add(lib.lookup("Sybil Eye").get(i));
        }

        assertEquals(lib.lookup("Sybil Eye"), ex);

        lib.checkin("Sybil Eye");

        assertEquals(lib.lookup("Sybil Eye"), new ArrayList<>());
    }

    @Test
    public void stringLibraryTest() {
        // test a library that uses names (String) to id patrons
        Library<String> lib = new Library<>();
        lib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib.add(9780446580342L, "David Baldacci", "Simple Genius");

        String patron1 = "Jane Doe";

        assertTrue(lib.checkout(9780330351690L, patron1, 1, 1, 2008));
        assertTrue(lib.checkout(9780374292799L, patron1, 1, 1, 2008));

        var booksCheckedOut1 = lib.lookup(patron1);
        assertEquals(booksCheckedOut1.size(), 2);
        assertTrue(booksCheckedOut1.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
        assertTrue(booksCheckedOut1.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
        assertEquals(booksCheckedOut1.get(0).getHolder(), patron1);
        assertEquals(booksCheckedOut1.get(0).getDueDate(), new GregorianCalendar(2008, 1, 1));
        assertEquals(booksCheckedOut1.get(1).getHolder(), patron1);
        assertEquals(booksCheckedOut1.get(1).getDueDate(), new GregorianCalendar(2008, 1, 1));

        assertTrue(lib.checkin(patron1));

    }

    @Test
    public void phoneNumberTest() {
        // test a library that uses phone numbers (PhoneNumber) to id patrons
        var lib = new Library<PhoneNumber>();
        lib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib.add(9780446580342L, "David Baldacci", "Simple Genius");

        PhoneNumber patron2 = new PhoneNumber("801.555.1234");

        assertTrue(lib.checkout(9780330351690L, patron2, 1, 1, 2008));
        assertTrue(lib.checkout(9780374292799L, patron2, 1, 1, 2008));

        ArrayList<LibraryBook<PhoneNumber>> booksCheckedOut2 = lib.lookup(patron2);

        assertEquals(booksCheckedOut2.size(), 2);
        assertTrue(booksCheckedOut2.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
        assertTrue(booksCheckedOut2.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
        assertEquals(booksCheckedOut2.get(0).getHolder(), patron2);
        assertEquals(booksCheckedOut2.get(0).getDueDate(), new GregorianCalendar(2008, 1, 1));
        assertEquals(booksCheckedOut2.get(1).getHolder(), patron2);
        assertEquals(booksCheckedOut2.get(1).getDueDate(), new GregorianCalendar(2008, 1, 1));

        assertTrue(lib.checkin(patron2));
    }

    @Test
    public void phoneNumberNoCheckoutsTest() {
        // test a library that uses phone numbers (PhoneNumber) to id patrons
        var lib = new Library<PhoneNumber>();

        PhoneNumber patron2 = new PhoneNumber("801.555.1234");

        ArrayList<LibraryBook<PhoneNumber>> booksCheckedOut2 = lib.lookup(patron2);

        assertEquals(booksCheckedOut2.size(), 0);
        assertEquals(booksCheckedOut2, new ArrayList<LibraryBook<PhoneNumber>>());

        assertTrue(!lib.checkin(patron2));
    }

    @Test
    public void orderByAuthorTest() {
        var lib = new Library<String>();
//        lib.addAll("src/assignment02/Mushroom_Publishing.txt");
        lib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
        lib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
        lib.add(9780446580342L, "Thomas L. Friedman", "Simple Genius except it's by Thomas L. Friedman");

        ArrayList<LibraryBook<String>> booksOrdered = lib.getOrderedByAuthor();
        LibraryBook<String> lastBook = null;

        for (LibraryBook<String> book : booksOrdered) {
            if (lastBook == null) {
                lastBook = book;
                continue;
            }
            if (book.getAuthor().equals(lastBook.getAuthor())) {
                assertTrue(book.getTitle().compareTo(lastBook.getTitle()) > 0);
            } else {
                assertTrue(book.getAuthor().compareTo(lastBook.getAuthor()) > 0);
            }
            lastBook = book;
        }
    }

    @Test
    public void orderByAuthorEmptyLibraryTest() {
        var lib = new Library<String>();

        ArrayList<LibraryBook<String>> booksOrdered = lib.getOrderedByAuthor();
        LibraryBook<String> lastBook = null;

        for (LibraryBook<String> book : booksOrdered) {
            if (lastBook == null) {
                lastBook = book;
                continue;
            }
            if (book.getAuthor().equals(lastBook.getAuthor())) {
                assertTrue(book.getTitle().compareTo(lastBook.getTitle()) > 0);
            } else {
                assertTrue(book.getAuthor().compareTo(lastBook.getAuthor()) > 0);
            }
            lastBook = book;
        }
    }

    @Test
    public void orderByOverdueBooksTest() {
        var lib = new Library<String>();
        lib.addAll("src/assignment02/Mushroom_Publishing.txt");

        lib.checkout(9781843190028L, "Sybil Eye", 11, 11, 2025);
        lib.checkout(9781843190042L, "Sybil Eye", 11, 12, 2024);
        lib.checkout(9781843190073L, "Sybil Eye", 11, 14, 2025);
        lib.checkout(9781843190110L, "Sybil Eye", 11, 19, 2024);
        lib.checkout(9781843190110L, "Sybil Eye", 1, 4, 2026);
        lib.checkout(9781843190110L, "Sybil Eye", 11, 19, 2025);

        ArrayList<LibraryBook<String>> booksOrdered = lib.getOverdueList(11, 14, 2025);
        LibraryBook<String> lastBook = null;

        for (LibraryBook<String> book : booksOrdered) {
            if (lastBook == null) {
                lastBook = book;
                continue;
            }
            if (book.getDueDate().equals(lastBook.getDueDate())) {
                assertTrue(book.getTitle().compareTo(lastBook.getTitle()) > 0);
            } else {
                assertTrue(book.getDueDate().compareTo(lastBook.getDueDate()) > 0);
            }
            lastBook = book;
        }
    }
    @Test
    public void noOverdueBooksTest() {
        var lib = new Library<String>();
        lib.addAll("src/assignment02/Mushroom_Publishing.txt");

        lib.checkout(9781843190028L, "Sybil Eye", 11, 11, 2026);
        lib.checkout(9781843190042L, "Sybil Eye", 11, 12, 2026);
        lib.checkout(9781843190073L, "Sybil Eye", 11, 14, 2026);
        lib.checkout(9781843190110L, "Sybil Eye", 11, 19, 2027);
        lib.checkout(9781843190110L, "Sybil Eye", 1, 4, 2026);
        lib.checkout(9781843190110L, "Sybil Eye", 11, 19, 2025);

        ArrayList<LibraryBook<String>> booksOrdered = lib.getOverdueList(11, 14, 2025);
        LibraryBook<String> lastBook = null;

        for (LibraryBook<String> book : booksOrdered) {
            if (lastBook == null) {
                lastBook = book;
                continue;
            }
            if (book.getDueDate().equals(lastBook.getDueDate())) {
                assertTrue(book.getTitle().compareTo(lastBook.getTitle()) > 0);
            } else {
                assertTrue(book.getDueDate().compareTo(lastBook.getDueDate()) > 0);
            }
            lastBook = book;
        }
        assertEquals(new ArrayList<>(), booksOrdered);
    }
}