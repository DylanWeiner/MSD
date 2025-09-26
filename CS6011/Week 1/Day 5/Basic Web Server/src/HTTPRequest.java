import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class HTTPRequest {

    Socket socketToClient;

    public HTTPRequest(Socket socketToClient) {
        this.socketToClient = socketToClient;
    }

    public String HTTPRequest() throws IOException {
            InputStream is = socketToClient.getInputStream();
            Scanner lineIntake = new Scanner(is);

            String line = lineIntake.nextLine(); // retrieves "GET / HTTP/1.1"
            String[] split = line.split(" ");
            return split[1];
    }
}