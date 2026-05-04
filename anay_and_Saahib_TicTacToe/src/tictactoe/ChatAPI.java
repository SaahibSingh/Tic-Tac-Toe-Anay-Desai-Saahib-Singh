package tictactoe;

//Imports
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.*;

public class ChatAPI {
    private static final List<String> messages = new ArrayList<>();
    public static void handleChatGet(HttpExchange ex) throws IOException {
        Map<String, Object> json = new HashMap<>();
        json.put("messages", messages);
        Json.send(ex, json);
    }

    public static void handleChatPost(HttpExchange ex) throws IOException {
        Map<String, String> form = Query.parseForm(ex);
        String msg = form.get("message");
        if (msg != null && !msg.isBlank()) messages.add(msg);
        ex.sendResponseHeaders(200, -1);
    }
}
