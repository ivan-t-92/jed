package ex1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private final ServerSocket serverSocket;

    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(4999);
    }

    public void runServer() {
        System.out.println("Accepting thread started");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Client receiver = Client.fromSocket(socket);
                ClientsContainer.CLIENTS.put(receiver.name, receiver);
                new Thread(receiver).start();
            } catch (IOException e) {
                System.out.println("Error while accepting");
                e.printStackTrace();
                break;
            }
            System.out.println("Accepted");
        }
        System.out.println("Accepting thread ended");
    }


}
