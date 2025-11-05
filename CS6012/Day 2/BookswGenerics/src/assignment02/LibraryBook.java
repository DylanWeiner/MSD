package assignment02;

import java.util.GregorianCalendar;

public class LibraryBook<Type> extends Book {
    boolean checkedOut;
    Type holder;
    GregorianCalendar dueDate = null;

    public LibraryBook(long isbn, String author, String title) {
        super(isbn, author, title);
    }

    public Type getHolder() {
        if(checkedOut) {
            return holder;
        }
        return null;
    }

    public GregorianCalendar getDueDate() {
        return this.dueDate;
    }
}
