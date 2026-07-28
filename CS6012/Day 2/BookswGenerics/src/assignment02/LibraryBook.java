package assignment02;

import java.util.GregorianCalendar;

public class LibraryBook<Type> extends Book {
    boolean checkedOut = false;
    Type holder = null;
    GregorianCalendar dueDate = null;

    public LibraryBook(long isbn, String author, String title) {
        super(isbn, author, title);
    }

    public Type getHolder() {
        return holder;
    }

    public GregorianCalendar getDueDate() {
        return this.dueDate;
    }
}
