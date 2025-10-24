import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class WebServer {
    static ArrayList<Socket> handlers = new ArrayList<>();

    public static void main() throws IOException, NoSuchAlgorithmException {

        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            final Socket socketToClient = serverSocket.accept();

            HandleClientConnect handler = new HandleClientConnect(socketToClient);

            handlers.add(socketToClient);

            Thread handlerThread = new Thread(handler);

            handlerThread.start();
        }
    }
}

