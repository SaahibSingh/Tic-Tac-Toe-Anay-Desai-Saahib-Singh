package tictactoe;

import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import java.io.IOException;

public class Json {
    private static final Gson gson = new Gson();

    public static void send(HttpExchange ex, Object data) throws IOException {
        String json = gson.toJson(data);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(200, json.length());
        ex.getResponseBody().write(json.getBytes());
        ex.close();
    }
}
