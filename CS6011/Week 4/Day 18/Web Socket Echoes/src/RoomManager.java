import javax.swing.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomManager {
    static Map<String, Room> rooms = new HashMap<>();

    public static class clientsSocket {
        static String username;
        static Socket userSocket;
    }

    public static class Room {
        String roomName;
        ArrayList<String> Clients = new ArrayList<>();
        ArrayList<Socket> clientsSockets = new ArrayList<>();

        Room(String roomName) {
            this.roomName = roomName;
        }
    }

    public static synchronized void assignClient(String name, Socket socket) {
        clientsSocket.username = name;
        clientsSocket.userSocket = socket;
    }

    public static synchronized void addRoomOrClient(String room) {
        Room r = rooms.get(room);
        if (r == null) {
            r = new Room(room);
            rooms.put(room, r);
            r.Clients.add(clientsSocket.username);
            r.clientsSockets.add(clientsSocket.userSocket);
        } else if (r.Clients.contains(clientsSocket.username)) {
            JOptionPane.showMessageDialog(null, "This name is already taken!", "Uh-Oh", JOptionPane.WARNING_MESSAGE);
            return;
        } else {
            r.Clients.add(clientsSocket.username);
            r.clientsSockets.add(clientsSocket.userSocket);
        }
    }

    public static synchronized void removeClient(String room) {
        Room r = rooms.get(room);
        if (r != null) {
            r.Clients.remove(clientsSocket.username);
            r.clientsSockets.remove(clientsSocket.userSocket);
            if (r.Clients.isEmpty()) {
                rooms.remove(room);
            }
        }
    }

    public static synchronized void sendMessage(String room, byte[] message, byte b1, byte b2, String json) throws IOException {
        Room r = rooms.get(room);
        if (r != null) {
            for (Socket can : r.clientsSockets) {
                DataOutputStream out = new DataOutputStream(can.getOutputStream());
                out.write(b1);
                out.write(b2);
                System.out.println("bytes " + json);
                out.write(message);
                out.flush();
            }
        }
    }
}