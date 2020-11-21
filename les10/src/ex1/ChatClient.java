package ex1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private final Socket socket;
    private final ObjectInputStream objectInputStream;
    private final ObjectOutputStream objectOutputStream;

    public ChatClient(String name) throws IOException {
        socket = new Socket("localhost", 4999);
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeUTF(name);
        objectOutputStream.flush();
    }

    public void runClient() {
        new Thread(() -> {
            try {
                while (true) {
                    MessageData messageData = (MessageData) objectInputStream.readObject();
                    System.out.println("[" + messageData.sender + "]: " + messageData.message);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error receiving messages");
                e.printStackTrace();
            }
        }).start();

        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String message = scanner.nextLine();
                message = message.strip();
                if (message.isEmpty()) {
                    continue;
                }
                if (message.equals("quit")) {
                    System.out.println("Closing...");
                    objectInputStream.close();
                    break;
                }
                String recipient = null;
                if (message.charAt(0) == '@') {
                    int spaceIndex = message.indexOf(' ');
                    if (spaceIndex >= 0) {
                        recipient = message.substring(1, spaceIndex);
                        message = message.substring(spaceIndex);
                        message = message.stripLeading();
                    }
                }
                if (!message.isEmpty()) {
                    objectOutputStream.writeObject(new MessageData(message, null, recipient));
                }
            }
        } catch (IOException e) {
            System.out.println("Error receiving messages");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.print("Ваше имя: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        try {
            ChatClient chatClient = new ChatClient(name);
            chatClient.runClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
