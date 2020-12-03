package ex1;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) {
        try {
            ChatServer chatServer = new ChatServer();
            chatServer.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
