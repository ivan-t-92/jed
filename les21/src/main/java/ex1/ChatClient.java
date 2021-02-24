package ex1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private final Socket socket;
    private final ObjectInputStream objectInputStream;
    private final DataOutputStream objectOutputStream;

    public ChatClient(String name) throws IOException {
        socket = new Socket("localhost", 4999);
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new DataOutputStream(socket.getOutputStream());
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
                objectOutputStream.writeUTF(message);
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
