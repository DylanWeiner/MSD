import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    static void main() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                    Socket socketToClient = serverSocket.accept(); // This returns the socket.
                    HTTPRequest socketRequest = new HTTPRequest(socketToClient);
                    String filename = socketRequest.getRequest();
                    servePage socketRespond = new servePage(socketToClient, filename);
            }
        }
        catch(IOException e) {
            System.out.println("IO Error." + e.getMessage());
        }
    }
}