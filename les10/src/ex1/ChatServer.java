package ex1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private final ServerSocket serverSocket;
    private final ArrayList<Client> clients = new ArrayList<>();

    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(4999);
    }

    public void runServer() {
        System.out.println("Accepting thread started");
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("Error while accepting");
                e.printStackTrace();
                break;
            }
            System.out.println("Accepted");

            ObjectInputStream objectInputStream;
            ObjectOutputStream objectOutputStream;
            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("Can not get streams");
                break;
            }

            System.out.println("Asking a name...");
            String name;
            try {
                name = objectInputStream.readUTF();
            } catch (IOException e) {
                System.out.println("Error asking a name");
                e.printStackTrace();
                break;
            }

            System.out.println("Got \"" + name + "\"");

            Client receiver = new Client(objectInputStream, objectOutputStream, name);
            clients.add(receiver);
            new Thread(receiver).start();
        }
        System.out.println("Accepting thread ended");
    }

    private void transmitMessage(Client receiver, MessageData messageData) {
        String to = messageData.recipient != null ? messageData.recipient : "<all>";
        System.out.println(messageData.sender + " -> " + to + ": " + messageData.message);

        if (messageData.recipient != null) {
            for (Client client : clients) {
                if (client != receiver && client.name.equals(messageData.recipient)) {
                    try {
                        client.objectOutputStream.writeObject(messageData);
                    } catch (IOException e) {
                        System.out.println("Error sending message to client");
                        e.printStackTrace();
                    }
                    break;
                }
            }
        } else {
            for (Client client : clients) {
                if (client != receiver) {
                    try {
                        client.objectOutputStream.writeObject(messageData);
                    } catch (IOException e) {
                        System.out.println("Error sending message to client");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class Client implements Runnable {
        final ObjectInputStream objectInputStream;
        final ObjectOutputStream objectOutputStream;
        final String name;

        Client(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream, String name) {
            this.objectInputStream = objectInputStream;
            this.objectOutputStream = objectOutputStream;
            this.name = name;
        }

        public void run() {
            System.out.println("Message receiving thread started for \"" + name + "\"");
            while (true) {
                MessageData messageData;
                try {
                    messageData = ((MessageData) objectInputStream.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error receiving message");
                    e.printStackTrace();
                    break;
                }

                messageData.sender = name;
                transmitMessage(this, messageData);
            }
            System.out.println("Message receiving thread ended");
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer chatServer = new ChatServer();
            chatServer.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
