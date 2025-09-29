import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class servePage {
    public servePage(Socket socketToClient, String filename) throws IOException {
        OutputStream out = socketToClient.getOutputStream();
        PrintWriter writer = new PrintWriter(out);

        // Send response header.
        String fileContents;
        //Serves main page
        if (filename.equals("/")) {
            fileContents = Files.readString(Path.of("index.html"));
            writer.print("HTTP/1.1 200 OK\n");
            writer.print("Content-Length: " + fileContents.length() + "\n");
            writer.print("Content-Type: text/html\n");
            writer.print("\r\n");
            writer.print(fileContents);
            writer.flush();
        } else
            // Looks for css files or other pages.
            try {
                fileContents = Files.readString(Path.of("styles.css"));
                writer.print("HTTP/1.1 200 OK\n");
                writer.print("Content-length: " + fileContents.length() + "\n");
                writer.print("Content-Type: text/css\n");
                writer.print("\r\n");
                writer.print(fileContents);
                writer.flush();
            } catch (NoSuchFileException e) {
                writer.print("HTTP 1.1 404 not found");
                writer.flush();
            }
        writer.close();
    }
}
