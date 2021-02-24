package ex1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientsContainer {

    public static final Map<String, Client> CLIENTS = new ConcurrentHashMap<>();
}
