package assignment05;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class WebBrowserTest {
    WebBrowser emptyData;
    WebBrowser data;

    @BeforeEach
    void setUp() throws MalformedURLException {
        emptyData = new WebBrowser();
        data = new WebBrowser();

        URL webpage1 = new URL("http://www.google.com");
        URL webpage2 = new URL("http://www.youtube.com");
        URL webpage3 = new URL("http://www.wikipedia.org");
        URL webpage4 = new URL("http://www.amazon.com");
        URL webpage5 = new URL("http://www.slack.com");

        data.visit(webpage1);
        data.visit(webpage2);
        data.visit(webpage3);
        data.visit(webpage4);
        data.visit(webpage5);
    }

    @Test
    void visit() throws MalformedURLException {
        URL webpage = new URL("http://www.google.com");
        emptyData.visit(webpage);
    }

    @Test
    void back() {
        System.out.println(data.back());
        System.out.println(data.back());
    }

    @Test
    void forward() {
        System.out.println(data.back());
        System.out.println(data.back());
        System.out.println(data.forward());
        System.out.println(data.forward());
    }

    @Test
    void history() {
        System.out.println("\n\n");
        for(URL u : data.history()) {
            System.out.println(u);
        }
    }
}