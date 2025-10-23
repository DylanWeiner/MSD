import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

public class WebServer {

    public static void main() throws IOException, NoSuchAlgorithmException {

        ServerSocket serverSocket = new ServerSocket(8080);

        HashMap<String, String> httpHeaders = new HashMap<>();

        while (true) {
            final Socket socketToClient = serverSocket.accept();
            InputStream is = socketToClient.getInputStream();
            Scanner lineIntake = new Scanner(is);
            OutputStream outStream = socketToClient.getOutputStream();
            PrintWriter writer = new PrintWriter(outStream);
            if (lineIntake.nextLine().contains("GET"))
                while (lineIntake.hasNextLine()) {
                    String line = lineIntake.nextLine();
                    if (line.isEmpty()) break;
                    System.out.println(line);
                    String[] splitLine = line.split("");
                    httpHeaders.put(splitLine[0], splitLine[1].trim());
                }

            System.out.println(httpHeaders);
            if (httpHeaders.containsKey("Connection")) {
                String key = httpHeaders.get("Sec-WebSocket-Key");
                System.out.println(key);
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                String secret = Base64.getEncoder().encodeToString(md.digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()));

                outStream.write("HTTP/1.1 101 Switching Protocols\r\n".getBytes());
                outStream.write("Upgrade: websocket\r\n".getBytes());
                outStream.write("Connection: Upgrade\r\n".getBytes());
                outStream.write(("\r\n").getBytes());
                outStream.flush();

                System.out.println("handshake is done");
                while (true) {
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    byte wsFirstFrameByte = in.readByte();

                }
            }

            // There is code to solve here.

            String result = new String(payLoad, "UTF-8");
            System.out.println(result);
        }
    }
}

