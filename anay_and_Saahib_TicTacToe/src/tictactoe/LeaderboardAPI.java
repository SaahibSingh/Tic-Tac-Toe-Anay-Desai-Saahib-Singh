package tictactoe;

//Imports
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.*;
public class LeaderboardAPI {
    private static final List<String> leaderboard = new ArrayList<>();
    public static void handleLeaderboard(HttpExchange ex) throws IOException {
        Map<String, Object> json = new HashMap<>();
        json.put("rows", leaderboard);
        Json.send(ex, json);
    }

    public static void addResult(String result) { leaderboard.add(result); }
}
