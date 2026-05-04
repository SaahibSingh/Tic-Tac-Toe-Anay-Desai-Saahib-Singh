package tictactoe;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.*;

public class BackendAPI {

    private static final GameLogic logic = new GameLogic();
    private static final Board board = new Board();
    private static final List<String> leaderboard = new ArrayList<>();
    private static final List<String> chat = new ArrayList<>();
    private static final Queue<String> queue = new LinkedList<>();

    // ---------------- GAME ----------------

    public static void gameState(HttpExchange ex) throws IOException {
        Map<String, Object> json = new HashMap<>();
        json.put("grid", board.getGrid());
        json.put("message", "");
        json.put("gameOver", false);
        Json.send(ex, json);
    }

    public static void gameMove(HttpExchange ex) throws IOException {
        Map<String, String> q = Query.parse(ex.getRequestURI().getQuery());
        int r = Integer.parseInt(q.get("row"));
        int c = Integer.parseInt(q.get("col"));

        char current = logic.getCurrentPlayer(board);

        if (board.getCell(r, c) == 'E') {
            board.setCell(r, c, current);
        }

        Map<String, Object> json = new HashMap<>();
        json.put("grid", board.getGrid());

        if (logic.checkWin(board, current)) {
            json.put("message", "Player " + current + " wins!");
            json.put("gameOver", true);
            leaderboard.add("Winner: " + current);
        } else if (logic.isDraw(board)) {
            json.put("message", "It's a draw!");
            json.put("gameOver", true);
        } else {
            json.put("message", "");
            json.put("gameOver", false);
        }

        Json.send(ex, json);
    }

    // ---------------- LEADERBOARD ----------------

    public static void leaderboard(HttpExchange ex) throws IOException {
        Map<String, Object> json = new HashMap<>();
        json.put("rows", leaderboard);
        Json.send(ex, json);
    }

    // ---------------- CHAT ----------------

    public static void chatGet(HttpExchange ex) throws IOException {
        Map<String, Object> json = new HashMap<>();
        json.put("messages", chat);
        Json.send(ex, json);
    }

    public static void chatPost(HttpExchange ex) throws IOException {
        Map<String, String> form = Query.parseForm(ex);
        String msg = form.get("message");
        if (msg != null && !msg.isBlank()) chat.add(msg);
        ex.sendResponseHeaders(200, -1);
    }

    // ---------------- MATCHMAKING ----------------

    public static void matchmaking(HttpExchange ex) throws IOException {
        Map<String, String> q = Query.parse(ex.getRequestURI().getQuery());
        String user = q.get("user");

        Map<String, Object> json = new HashMap<>();

        if (queue.isEmpty()) {
            queue.add(user);
            json.put("opponent", null);
        } else {
            json.put("opponent", queue.poll());
        }

        Json.send(ex, json);
    }
}
