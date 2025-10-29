import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class RoomManager {
    static Map<String, Room> rooms = new HashMap<>();

    public static class ClientSocket {
        String username;
        Socket userSocket;

        public ClientSocket(String username, Socket socket) {
            this.username = username;
            this.userSocket = socket;
        }
    }

    public static class Room {
        String roomName;
        ArrayList<ClientSocket> clients = new ArrayList<>();

        Room(String roomName) {
            this.roomName = roomName;
        }
    }

    public static synchronized void addRoomOrClient(String room, ClientSocket client) {
        Room r = rooms.get(room);
        if (r == null) {
            r = new Room(room);
            rooms.put(room, r);
            r.clients.add(client);
        } else {
            boolean exists = r.clients.stream()
                    .anyMatch(c -> c.username.equals(client.username));
            if (exists) {
                JOptionPane.showMessageDialog(null, "This name is already taken!", "Uh-Oh", JOptionPane.WARNING_MESSAGE);
                return;
            }
            r.clients.add(client);
        }
    }

    public static synchronized void removeClient(String room, ClientSocket client) {
        Room r = rooms.get(room);
        if (r != null) {
            r.clients.removeIf(c -> c.username.equals(client.username) && c.userSocket.equals(client.userSocket));
            if (r.clients.isEmpty()) {
                rooms.remove(room);
            }
        }
    }

    public static synchronized void removeClientFromAllRooms(ClientSocket client) {
        ArrayList<String> emptyRooms = new ArrayList<>();

        for (Map.Entry<String, Room> entry : rooms.entrySet()) {
            Room room = entry.getValue();
            room.clients.removeIf(c -> c.username.equals(client.username) && c.userSocket.equals(client.userSocket));
            if (room.clients.isEmpty()) {
                emptyRooms.add(entry.getKey());
            }
        }

        // Remove any rooms that are now empty
        for (String roomName : emptyRooms) {
            rooms.remove(roomName);
        }
    }

    // Broadcast message to all clients in room
    public static synchronized void sendMessage(String room, byte[] message, byte b1, byte b2, String json) throws IOException {
        Room r = rooms.get(room);
        if (r != null) {
            for (ClientSocket client : r.clients) {
                try {
                    DataOutputStream out = new DataOutputStream(client.userSocket.getOutputStream());
                    out.write(b1);
                    out.write(b2);
                    System.out.println("Sending to " + client.username + ": " + json);
                    out.write(message);
                    out.flush();
                } catch(IOException e) {
                    System.err.println("Failed sending to client " + client.username);
                    e.printStackTrace();
                    // Optionally remove or mark client for cleanup
                }
            }
        }
    }
}