package ex1;

public class MessageData {
    public String message;
    public String sender;
    public String recipient;
    public Type type;

    public MessageData(String message, String sender, String recipient, Type type) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.type = type;
    }

    public enum Type {QUIT, ADDRESS, FOR_ALL}
}
