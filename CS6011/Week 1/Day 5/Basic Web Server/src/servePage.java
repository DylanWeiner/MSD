import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class servePage {
    Socket socketToClient;
    PrintWriter writer;
    // Send response header.
    String fileContents;
    OutputStream out;
    String filename;

    public servePage(Socket socketToClient, String filename) throws IOException {
        this.socketToClient = socketToClient;
        writer = null;
        // Send response header.
        this.out = socketToClient.getOutputStream();
        this.writer = new PrintWriter(out);
        this.filename = filename;

        retrieveMain();
    }

    public void retrieveOther() throws IOException {
        try {
            fileContents = Files.readString(Path.of(filename));
            writer.print("HTTP/1.1 200 OK\n");
            writer.print("Content-length: " + fileContents.length() + "\n");
            Matcher m = Pattern.compile("[^.]*$").matcher(filename);
            m.find();
            String fileType = m.group();
            writer.print("Content-Type: text/" + fileType + "\n");
            writer.print("\r\n");
            writer.print(fileContents);
            writer.flush();
        } catch (NoSuchFileException e) {
            writer.print("HTTP 1.1 404 not found");
            writer.flush();
        } catch(UnsupportedOperationException e) {
            writer.print("HTTP 1.1 405 Method Not Allowed");
        } catch(MalformedURLException e) {
            writer.print("HTTP 1.1 400 Bad Request");
        }
        writer.close();
    }

    public void retrieveMain() throws IOException {
        //Serves main page
        if (filename.equals("/")) {
            fileContents = Files.readString(Path.of("index.html"));
            writer.print("HTTP/1.1 200 OK\n");
            writer.print("Content-Length: " + fileContents.length() + "\n");
            writer.print("Content-Type: text/html\n");
            writer.print("\r\n");
            writer.print(fileContents);
            writer.flush();
        } else {
            retrieveOther();
        }
    }
}
