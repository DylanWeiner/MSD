import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;
import java.security.MessageDigest;
import java.util.Base64;

public class HandleClientConnect implements Runnable {
    private Socket clientSocket;
    HashMap<String, String> httpHeaders = new HashMap<>();

    public HandleClientConnect(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                InputStream is = clientSocket.getInputStream();
                Scanner lineIntake = new Scanner(is);
                OutputStream outStream = clientSocket.getOutputStream();
                PrintWriter writer = new PrintWriter(outStream)
        ) {
            String line = lineIntake.nextLine();  // GET / HTTP/1.1
            String[] split = line.split(" ");
            String filename = split[1];
            String fileContents;
            if (line.contains("GET"))
                while (lineIntake.hasNextLine()) {
                    line = lineIntake.nextLine();
                    if (line.isEmpty()) break;
                    System.out.println(line);
                    String[] splitLine = line.split(":");
                    httpHeaders.put(splitLine[0], splitLine[1].trim());
                }

            if(!httpHeaders.containsKey("Sec-WebSocket-Key")) {
                if (filename.equals("/")) {
                    try {
                        fileContents = Files.readString(Path.of("index.html"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
            }

            else{
                System.out.println(httpHeaders);
        String key = httpHeaders.get("Sec-WebSocket-Key");
        System.out.println(key);
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA-1");
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
                String secret = Base64.getEncoder().encodeToString(md.digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()));

        outStream.write("HTTP/1.1 101 Switching Protocols\r\n".getBytes());
        outStream.write("Upgrade: websocket\r\n".getBytes());
        outStream.write("Connection: Upgrade\r\n".getBytes());
        outStream.write(("Sec-WebSocket-Accept: " + secret+"\r\n").getBytes());
        outStream.write(("\r\n").getBytes());
        outStream.flush();

        System.out.println("handshake is done");
        while(true) {
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            byte wsFirstFrameByte = in.readByte();
            int opcode = wsFirstFrameByte & 0x0F;
            int fin = (wsFirstFrameByte >> 7) & 0x01;
            byte wsSecondFrameByte = in.readByte();
            int maskBool = (wsSecondFrameByte >> 7) & 0x01;
            int payloadLength = (wsSecondFrameByte) & 0x7F;
            byte[] mask = in.readNBytes(4);
            byte[] payload = in.readNBytes(payloadLength);

            for(int i = 0; i < payloadLength; i++) {
                payload[i] = (byte) (payload[i]^mask[i & 0x03]);
            }
            String result = new String(payload, "UTF-8");
            System.out.println("result string:" + result);

            String[] parts = result.split(" ");

            RoomManager.assignClient(parts[1], clientSocket);

            String json = "";
            if(parts[0].equals("join")) {
                RoomManager.addRoomOrClient(parts[2]);
                json = String.format("{\"type\":\"%s\", \"user\":\"%s\",\"room\":\"%s\"}", parts[0], parts[1], parts[2]);
            } else if(parts[0].equals("leave")) {
                RoomManager.removeClient(parts[2]);
                json = String.format("{\"type\":\"%s\", \"user\":\"%s\",\"room\":\"%s\"}", parts[0], parts[1], parts[2]);
            }else {
                json = String.format("{\"type\":\"%s\", \"user\":\"%s\",\"room\":\"%s\",\"message\":\"%s\"}", parts[0], parts[1], parts[2], parts[3]);
            }

            byte wsFirstFrameReturn = -126;
            byte wsSecondFrameReturn = (byte) (json.getBytes(StandardCharsets.UTF_8).length);
            byte[] pay = json.getBytes(StandardCharsets.UTF_8);

            RoomManager.sendMessage(parts[2], pay, wsFirstFrameReturn, wsSecondFrameReturn, json);
        }
    }


} catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        finally{
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }
}
