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

    public static void main() throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            final Socket socketToClient = serverSocket.accept();
            new Thread( () -> {
                try (
                        InputStream is = socketToClient.getInputStream();
                        Scanner lineIntake = new Scanner(is);
                        OutputStream out = socketToClient.getOutputStream();
                        PrintWriter writer = new PrintWriter(out)
                ){
                    String line = lineIntake.nextLine();  // GET / HTTP/1.1
                    String[] split = line.split(" ");
                    String filename = split[1];
                    String fileContents;

                    if (filename.equals("/")) {
                        fileContents = Files.readString(Path.of("index.html"));
                        writer.print("HTTP/1.1 200 OK\r\n");
                        writer.print("Content-Length: " + fileContents.length() + "\r\n");
                        writer.print("Content-Type: text/html\r\n");
                        writer.print("\r\n");
                        writer.print(fileContents);
                    } else {
                        try {
                            // Serves the requested file directly (e.g., /styles.css)
                            String path = filename.startsWith("/") ? filename.substring(1) : filename;
                            fileContents = Files.readString(Path.of(path));
                            String contentType = filename.endsWith(".css") ? "text/css" : "text/plain";

                            writer.print("HTTP/1.1 200 OK\r\n");
                            writer.print("Content-Length: " + fileContents.length() + "\r\n");
                            writer.print("Content-Type: " + contentType + "\r\n");
                            writer.print("\r\n");
                            writer.print(fileContents);
                        } catch (NoSuchFileException e) {
                            writer.print("HTTP/1.1 404 Not Found\r\n");
                            writer.print("Content-Length: 0\r\n");
                            writer.print("\r\n");
                        }
                    }
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socketToClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

