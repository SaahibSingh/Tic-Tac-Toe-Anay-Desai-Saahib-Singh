package tictactoe;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TicTacToeWebApp {

    private static final int PORT = 8080;

    private static final UserStore userStore = new UserStore();
    private static final AuthService authService = new AuthService(userStore);
    private static final SessionManager sessions = new SessionManager();
    private static final WebRenderer renderer = new WebRenderer();
    private static final GameLogic logic = new GameLogic();

    // per-user board cache
    private static final Map<String, Board> boards = new HashMap<>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", TicTacToeWebApp::handleRoot);
        server.createContext("/login", TicTacToeWebApp::handleLogin);
        server.createContext("/signup", TicTacToeWebApp::handleSignup);
        server.createContext("/game", TicTacToeWebApp::handleGame);
        server.createContext("/reset", TicTacToeWebApp::handleReset);
        server.createContext("/logout", TicTacToeWebApp::handleLogout);
        server.setExecutor(null);
        System.out.println("Server running on http://localhost:" + PORT);
        server.start();
    }

    // ---------- Helpers ----------

    private static String getSessionToken(HttpExchange ex) {
        List<String> cookies = ex.getRequestHeaders().get("Cookie");
        if (cookies == null) return null;
        for (String header : cookies) {
            String[] parts = header.split(";");
            for (String p : parts) {
                String[] kv = p.trim().split("=", 2);
                if (kv.length == 2 && kv[0].equals("SESSION")) {
                    return kv[1];
                }
            }
        }
        return null;
    }

    private static String getLoggedInUser(HttpExchange ex) {
        String token = getSessionToken(ex);
        if (token == null) return null;
        return sessions.getUser(token);
    }

    private static void sendResponse(HttpExchange ex, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static Map<String, String> parseForm(HttpExchange ex) throws IOException {
        InputStream is = ex.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> map = new HashMap<>();
        for (String pair : body.split("&")) {
            if (pair.isEmpty()) continue;
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String val = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            map.put(key, val);
        }
        return map;
    }

    private static Map<String, String> parseQuery(String query) throws IOException {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;
        for (String pair : query.split("&")) {
            if (pair.isEmpty()) continue;
            String[] kv = pair.split("=", 2);
            String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8);
            String val = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
            map.put(key, val);
        }
        return map;
    }

    private static Board getBoardForUser(String username) {
        return boards.computeIfAbsent(username, u -> {
            String filename = "board_" + u + ".csv";
            ensureBoardFile(filename);
            Board b = new Board(filename);
            b.clearBoard();
            return b;
        });
    }

    private static void ensureBoardFile(String filename) {
        try {
            File f = new File("src/tictactoe/" + filename);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                try (FileWriter w = new FileWriter(f)) {
                    w.write("E,E,E\nE,E,E\nE,E,E");
                }
            }
        } catch (IOException ignored) {}
    }

    private static char computeCurrentPlayer(Board board) {
        char[][] g = board.getGrid();
        int x = 0, o = 0;
        for (int r = 0; r < g.length; r++) {
            for (int c = 0; c < g[0].length; c++) {
                if (board.getCell(r, c) == 'X') x++;
                if (board.getCell(r, c) == 'O') o++;
            }
        }
        return (x == o) ? 'X' : 'O';
    }

    // ---------- Handlers ----------

    private static void handleRoot(HttpExchange ex) throws IOException {
        String user = getLoggedInUser(ex);
        if (user == null) {
            ex.getResponseHeaders().add("Location", "/login");
            ex.sendResponseHeaders(302, -1);
            ex.close();
        } else {
            ex.getResponseHeaders().add("Location", "/game");
            ex.sendResponseHeaders(302, -1);
            ex.close();
        }
    }

    private static void handleLogin(HttpExchange ex) throws IOException {
        if (ex.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(ex, renderer.loginPage(null));
            return;
        }
        if (ex.getRequestMethod().equalsIgnoreCase("POST")) {
            Map<String, String> form = parseForm(ex);
            String username = form.getOrDefault("username", "").trim();
            String password = form.getOrDefault("password", "");

            if (authService.authenticate(username, password)) {
                String token = sessions.createSession(username);
                ex.getResponseHeaders().add("Set-Cookie", "SESSION=" + token + "; Path=/; HttpOnly");
                ex.getResponseHeaders().add("Location", "/game");
                ex.sendResponseHeaders(302, -1);
                ex.close();
            } else {
                sendResponse(ex, renderer.loginPage("Invalid credentials."));
            }
        }
    }

    private static void handleSignup(HttpExchange ex) throws IOException {
        if (ex.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(ex, renderer.signupPage(null));
            return;
        }
        if (ex.getRequestMethod().equalsIgnoreCase("POST")) {
            Map<String, String> form = parseForm(ex);
            String username = form.getOrDefault("username", "").trim();
            String password = form.getOrDefault("password", "");

            if (username.isEmpty() || password.isEmpty()) {
                sendResponse(ex, renderer.signupPage("All fields are required."));
                return;
            }
            if (!authService.register(username, password)) {
                sendResponse(ex, renderer.signupPage("Username already taken."));
                return;
            }
            ex.getResponseHeaders().add("Location", "/login");
            ex.sendResponseHeaders(302, -1);
            ex.close();
        }
    }

    private static void handleGame(HttpExchange ex) throws IOException {
        String user = getLoggedInUser(ex);
        if (user == null) {
            ex.getResponseHeaders().add("Location", "/login");
            ex.sendResponseHeaders(302, -1);
            ex.close();
            return;
        }

        Board board = getBoardForUser(user);
        String message = null;
        boolean gameOver = false;

        if (ex.getRequestMethod().equalsIgnoreCase("GET")) {
            Map<String, String> q = parseQuery(ex.getRequestURI().getQuery());
            if (q.containsKey("row") && q.containsKey("col")) {
                int r = Integer.parseInt(q.get("row"));
                int c = Integer.parseInt(q.get("col"));
                if (r >= 0 && r <= 2 && c >= 0 && c <= 2 && board.getCell(r, c) == 'E') {
                    char current = computeCurrentPlayer(board);
                    board.setCell(r, c, current);
                    if (logic.checkWin(board, current)) {
                        message = "Player " + current + " wins!";
                        gameOver = true;
                    } else if (logic.isDraw(board)) {
                        message = "It's a draw!";
                        gameOver = true;
                    }
                }
            }
        }

        sendResponse(ex, renderer.gamePage(user, board, message, gameOver));
    }

    private static void handleReset(HttpExchange ex) throws IOException {
        String user = getLoggedInUser(ex);
        if (user != null) {
            Board b = getBoardForUser(user);
            b.clearBoard();
        }
        ex.getResponseHeaders().add("Location", "/game");
        ex.sendResponseHeaders(302, -1);
        ex.close();
    }

    private static void handleLogout(HttpExchange ex) throws IOException {
        String token = getSessionToken(ex);
        if (token != null) sessions.destroy(token);
        ex.getResponseHeaders().add("Set-Cookie", "SESSION=deleted; Path=/; Max-Age=0");
        ex.getResponseHeaders().add("Location", "/login");
        ex.sendResponseHeaders(302, -1);
        ex.close();
    }
}
