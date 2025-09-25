import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Scanner;

public class WebServer {

//    ServerSocket serverSocket;

    static void main() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            Socket socketToClient = serverSocket.accept(); // This returns the socket.
            InputStream is = socketToClient.getInputStream();
            Scanner lineIntake = new Scanner(is);

            String line = lineIntake.nextLine(); // retrieves "GET / HTTP/1.1"
            String[] split = line.split(" ");
            String filename = split[1];


            OutputStream out = socketToClient.getOutputStream();
            PrintWriter writer = new PrintWriter(out);

            // Send response header.
            String fileContents;
            if (filename.equals("/")) {
                fileContents = Files.readString(Path.of("index.html"));
                writer.print("HTTP/1.1 200 OK\n");
                writer.print("Content-Length: " + fileContents.length() + "\n");
                writer.print("Content-Type: text/html\n");
                writer.print("\r\n");
                writer.print(fileContents);
                writer.flush();
            } else
                try {
                    fileContents = Files.readString(Path.of("styles.css"));
                    writer.print("HTTP/1.1 200 OK\n");
                    writer.print("Content-length: " + fileContents.length() + "\n");
                    writer.print("Content-Type: text/css\n");
                    writer.print("\r\n");
                    writer.print(fileContents);
                    writer.flush();
                }
                catch(NoSuchFileException e) {
                    writer.print("http 1.1 404 not found");
                    writer.flush();
                }
            writer.close();
            }
        }
    }

