package assignment05;

import java.net.URL;
import java.util.NoSuchElementException;

public class WebBrowser {
    private LinkedListStack<URL> URLListForward;
    private LinkedListStack<URL> URLListBackward;
    SinglyLinkedList<URL> history;
    private URL currentURL;

    public WebBrowser() {
        URLListForward = new LinkedListStack<>();
        URLListBackward = new LinkedListStack<>();
        history = new SinglyLinkedList<>();
    }

    public WebBrowser(SinglyLinkedList<URL> history) {
        history = this.history;
    }

    public void visit(URL webpage) {
        URLListForward.clear();
        URLListBackward.push(currentURL);
        currentURL = webpage;
        history.insertFirst(currentURL);
    }

    public URL back() throws NoSuchElementException {
        if(URLListBackward.isEmpty()) {
            throw new NoSuchElementException();
        }
        URLListForward.push(currentURL);
        currentURL = URLListBackward.peek();
        URLListBackward.pop();
        return currentURL;
    }

    public URL forward() throws NoSuchElementException {
        if(URLListForward.isEmpty()) {
            throw new NoSuchElementException();
        }
        URLListBackward.push(currentURL);
        currentURL = URLListForward.peek();
        URLListForward.pop();
        return currentURL;
    }

    public SinglyLinkedList<URL> history() {
        return history;
    }
}
