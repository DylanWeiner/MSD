package assignment05;

import java.net.URL;
import java.util.NoSuchElementException;

public class WebBrowser {
    private LinkedListStack<URL> URLListForward = new LinkedListStack<>();
    private LinkedListStack<URL> URLListBackward = new LinkedListStack<>();
    SinglyLinkedList<URL> history =  new SinglyLinkedList<>();
    private URL currentURL = null;

    public WebBrowser() {}

    public WebBrowser(SinglyLinkedList<URL> history) {
        if(history == null || history.isEmpty()) {
            return;
        }
        currentURL = history.getFirst();
        for(int i = history.size() - 1; i > 0; i--) {
            URLListBackward.push(history.get(i));
        }
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
