package ex1;

import java.io.Serializable;

public class MessageData implements Serializable {
    public String message;
    public String sender;
    public String recipient;

    public MessageData(String message, String sender, String recipient) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }
}
