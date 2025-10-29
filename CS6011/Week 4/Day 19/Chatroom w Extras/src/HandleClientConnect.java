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
    private RoomManager.ClientSocket client;
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

            if (line.contains("GET")) {
                while (lineIntake.hasNextLine()) {
                    line = lineIntake.nextLine();
                    if (line.isEmpty()) break;
                    System.out.println(line);
                    String[] splitLine = line.split(":");
                    httpHeaders.put(splitLine[0], splitLine[1].trim());
                }
            }

            if (!httpHeaders.containsKey("Sec-WebSocket-Key")) {
                // Handle normal HTTP requests (e.g., serving index.html or other files)
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

            } else {
                // WebSocket handshake
                System.out.println(httpHeaders);
                String key = httpHeaders.get("Sec-WebSocket-Key");
                System.out.println(key);
                MessageDigest md;
                try {
                    md = MessageDigest.getInstance("SHA-1");
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
                String secret = Base64.getEncoder().encodeToString(
                        md.digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes())
                );

                outStream.write("HTTP/1.1 101 Switching Protocols\r\n".getBytes());
                outStream.write("Upgrade: websocket\r\n".getBytes());
                outStream.write("Connection: Upgrade\r\n".getBytes());
                outStream.write(("Sec-WebSocket-Accept: " + secret + "\r\n").getBytes());
                outStream.write("\r\n".getBytes());
                outStream.flush();

                System.out.println("handshake is done");

                try {
                    while (true) {
                        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                        byte wsFirstFrameByte = in.readByte();
                        int opcode = wsFirstFrameByte & 0x0F;
                        int fin = (wsFirstFrameByte >> 7) & 0x01;
                        byte wsSecondFrameByte = in.readByte();
                        int maskBool = (wsSecondFrameByte >> 7) & 0x01;
                        int payloadLength = (wsSecondFrameByte) & 0x7F;
                        byte[] mask = in.readNBytes(4);
                        byte[] payload = in.readNBytes(payloadLength);

                        for (int i = 0; i < payloadLength; i++) {
                            payload[i] = (byte) (payload[i] ^ mask[i & 0x03]);
                        }
                        String result = new String(payload, StandardCharsets.UTF_8);
                        System.out.println("result string:" + result);

                        String[] parts = result.split(" ");
                        String command = parts[0];
                        String user = parts.length > 1 ? parts[1] : "";
                        String room = parts.length > 2 ? parts[2] : "";
                        String message = parts.length > 3 ? parts[3] : "";
                        String color = parts.length > 4 ? parts[4] : "black";

                        if (client == null) {
                            client = new RoomManager.ClientSocket(user, clientSocket);
                        } else {
                            client.username = user;
                        }

                        String json = "";



                        if(command.equals("join")) {
                            RoomManager.addRoomOrClient(room, client);
                            json = String.format("{\"type\":\"join\", \"user\":\"%s\", \"room\":\"%s\"}", user, room);
                        } else if (command.equals("leave")) {
                            RoomManager.removeClient(room, client);
                            json = String.format("{\"type\":\"leave\", \"user\":\"%s\", \"room\":\"%s\"}", user, room);
                        } else if (command.equals("message")) {
                            json = String.format("{\"type\":\"message\", \"user\":\"%s\", \"room\":\"%s\", \"message\":\"%s\", \"color\":\"%s\"}",
                                    user, room, message, color);
                        } else if (command.equals("color")) {
                            json = String.format("{\"type\":\"color\", \"user\":\"%s\", \"room\":\"%s\", \"color\":\"%s\"}",
                                    user, room, message);
                        }

                        byte wsFirstFrameReturn = -126;
                        byte wsSecondFrameReturn = (byte) (json.getBytes(StandardCharsets.UTF_8).length);
                        byte[] pay = json.getBytes(StandardCharsets.UTF_8);

                        RoomManager.sendMessage(room, pay, wsFirstFrameReturn, wsSecondFrameReturn, json);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        if (client != null) {
                            RoomManager.removeClientFromAllRooms(client);
                        }
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}