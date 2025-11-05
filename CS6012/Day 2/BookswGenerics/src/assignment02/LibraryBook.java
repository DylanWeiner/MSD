package assignment02;

import java.util.GregorianCalendar;

public class LibraryBook extends Book {
    boolean checkedOut;
    String holder;
    GregorianCalendar dueDate = new GregorianCalendar();

    public LibraryBook(long isbn, String author, String title) {
        super(isbn, author, title);
    }

    public String getHolder() {
        if(checkedOut) {
            return holder;
        }
        return null;
    }

    public GregorianCalendar getDueDate() {
        if(this.dueDate != null) {
            return this.dueDate;
        }
        return null;
    }
}
