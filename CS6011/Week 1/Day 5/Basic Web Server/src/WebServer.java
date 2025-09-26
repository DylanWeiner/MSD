import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    static void main() throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            try {
                Socket socketToClient = serverSocket.accept(); // This returns the socket.
                HTTPRequest socketRequest = new HTTPRequest(socketToClient);
                String filename = socketRequest.HTTPRequest();
                HTTPRespond socketRespond = new HTTPRespond(socketToClient, filename);
            }
            catch (Exception e) {
                System.out.println("Error with Client Request.");
            }
        }
    }
}

