import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class WebServer {
    static ArrayList<HandleClientConnect> handlers = new ArrayList<>();

    public static void main() throws IOException, NoSuchAlgorithmException {

        ServerSocket serverSocket = new ServerSocket(8080);

        HashMap<String, String> httpHeaders = new HashMap<>();

        while (true) {
            final Socket socketToClient = serverSocket.accept();

            HandleClientConnect handler = new HandleClientConnect(socketToClient);

            handlers.add(handler);

            Thread handlerThread = new Thread(handler);

            handlerThread.start();

        }
    }
}

