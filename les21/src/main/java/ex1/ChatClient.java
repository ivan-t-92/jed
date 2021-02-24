package ex1;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private final Socket socket;
    private final DataInputStream objectInputStream;
    private final DataOutputStream objectOutputStream;

    public ChatClient(String name) throws IOException {
        socket = new Socket("localhost", 4999);
        objectInputStream = new DataInputStream(socket.getInputStream());
        objectOutputStream = new DataOutputStream(socket.getOutputStream());
        objectOutputStream.writeUTF(name);
        objectOutputStream.flush();
    }

    public void runClient() {
        new Thread(() -> {
            Gson gson = new Gson();
            try {
                while (true) {
                    MessageData messageData = gson.fromJson(objectInputStream.readUTF(), MessageData.class);
                    System.out.println("[" + messageData.sender + "]: " + messageData.message);
                }
            } catch (IOException | JsonSyntaxException e) {
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
