package tictactoe;

//Imports
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameAPI {
    private static final GameLogic logic = new GameLogic();
    private static final Board board = new Board();
    public static void handleGameState(HttpExchange ex) throws IOException {
        Map<String, Object> json = new HashMap<>();
        json.put("grid", board.getGrid());
        json.put("message", "");
        json.put("gameOver", false);

        Json.send(ex, json);
    }

    public static void handleMove(HttpExchange ex) throws IOException {
        Map<String, String> q = Query.parse(ex.getRequestURI().getQuery());
        int r = Integer.parseInt(q.get("row"));
        int c = Integer.parseInt(q.get("col"));
        char current = logic.getCurrentPlayer(board);
        if (board.getCell(r, c) == 'E') board.setCell(r, c, current);

        Map<String, Object> json = new HashMap<>();
        json.put("grid", board.getGrid());

        if (logic.checkWin(board, current)) {
            json.put("message", "Player " + current + " wins!");
            json.put("gameOver", true);
        } else if (logic.isDraw(board)) {
            json.put("message", "It's a draw!");
            json.put("gameOver", true);
        } else {
            json.put("message", "");
            json.put("gameOver", false);
        }
        Json.send(ex, json);
    }
}
