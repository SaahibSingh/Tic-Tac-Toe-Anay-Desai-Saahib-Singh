package tictactoe;
import java.util.*; //Import
public class ChatServer {
    private final List<String> messages = new ArrayList<>();
    public synchronized void post(String user, String msg) {
        messages.add(user + ": " + msg);
        if (messages.size() > 100) messages.remove(0);
    }
    public synchronized List<String> getMessages() { return new ArrayList<>(messages); }
}
