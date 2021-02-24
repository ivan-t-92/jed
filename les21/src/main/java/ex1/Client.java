package ex1;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class Client implements Runnable {
    final Gson gson = new Gson();
    final Socket socket;
    final DataInputStream objectInputStream;
    final DataOutputStream objectOutputStream;
    final String name;

    private Client(Socket socket, DataInputStream objectInputStream, DataOutputStream objectOutputStream, String name) {

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.name = name;
    }

    public static Client fromSocket(Socket socket) throws IOException {
        var objectOutputStream = new DataOutputStream(socket.getOutputStream());
        var objectInputStream = new DataInputStream(socket.getInputStream());
        String name = objectInputStream.readUTF();
        return new Client(
                socket,
                objectInputStream,
                objectOutputStream,
                name);
    }

    public void run() {
        System.out.println("Message receiving thread started for \"" + name + "\"");
        while (true) {
            try {
                parseMessage(objectInputStream.readUTF()).ifPresent(this::processMessage);
            } catch (IOException e) {
                System.out.println("Error receiving message");
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Message receiving thread ended");
    }

    private void processMessage(MessageData messageData) {
        switch (messageData.type) {
            case QUIT -> quit(messageData);
            case ADDRESS -> sendDirectly(messageData);
            case FOR_ALL -> sendToAll(messageData);
        }
    }

    private void quit(MessageData messageData) {
        System.out.println("Closing...");

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClientsContainer.CLIENTS.remove(name);
    }

    private Optional<MessageData> parseMessage(String message) {
        MessageData messageData = null;
        if (message.equals("quit")) {
            messageData = new MessageData(null, this.name, null, MessageData.Type.QUIT);
        } else {
            String recipient = null;
            MessageData.Type type = MessageData.Type.FOR_ALL;
            if (message.charAt(0) == '@') {
                int spaceIndex = message.indexOf(' ');
                if (spaceIndex >= 0) {
                    recipient = message.substring(1, spaceIndex);
                    message = message.substring(spaceIndex);
                    message = message.stripLeading();
                    type = MessageData.Type.ADDRESS;
                }
            }
            if (!message.isEmpty()) {
                messageData = new MessageData(message, this.name, recipient, type);
            }
        }
        return Optional.ofNullable(messageData);
    }

    private void sendToAll(MessageData messageData) {
        System.out.println(messageData.sender + " -> " + "<all>" + ": " + messageData.message);

        ClientsContainer.CLIENTS.values().stream()
                .filter(client -> client != this)
                .forEach(client -> client.sendMessageToClient(messageData));
    }

    private void sendDirectly(MessageData messageData) {
        System.out.println(messageData.sender + " -> " + messageData.recipient + ": " + messageData.message);

        if (!name.equals(messageData.recipient) && ClientsContainer.CLIENTS.containsKey(messageData.recipient)) {
            ClientsContainer.CLIENTS.get(messageData.recipient)
                    .sendMessageToClient(messageData);
        }
    }

    private void sendMessageToClient(MessageData messageData) {
        try {
            objectOutputStream.writeUTF(gson.toJson(messageData));
        } catch (IOException e) {
            System.out.println("Error sending message to client");
            e.printStackTrace();
        }
    }
}
