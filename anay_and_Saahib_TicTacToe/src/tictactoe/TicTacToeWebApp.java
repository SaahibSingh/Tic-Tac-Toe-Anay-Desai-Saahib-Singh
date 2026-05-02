package tictactoe;

//Imports
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
    private static final Map<String, Board> boards = new HashMap<>(); // per-user board cache

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
        server.createContext("/login", ex -> serveStatic(ex, "login.html"));
        server.createContext("/signup", ex -> serveStatic(ex, "signup.html"));
        server.createContext("/game", ex -> serveStatic(ex, "game.html"));
        server.createContext("/leaderboard", ex -> serveStatic(ex, "leaderboard.html"));
        server.createContext("/chat", ex -> serveStatic(ex, "chat.html"));
        server.createContext("/matchmaking", ex -> serveStatic(ex, "matchmaking.html"));
        server.createContext("/leaderboard-json", ex -> {
            List<String> rows = leaderboard.getRankings();
            Map<String, Object> json = new HashMap<>();
            json.put("rows", rows);
            sendJson(ex, json);
        });

        server.createContext("/chat-json", ex -> {
            Map<String, Object> json = new HashMap<>();
            json.put("messages", chatServer.getMessages());
            sendJson(ex, json);
        });

        server.createContext("/chat-post", ex -> {
        String user = getLoggedInUser(ex);
        if (user == null) {
            ex.sendResponseHeaders(401, -1);
            return;
        });
    
        Map<String, String> form = parseForm(ex);
        chatServer.post(user, form.get("message"));
    
        ex.sendResponseHeaders(200, -1);
        });

        server.createContext("/react-dist", ex -> {
            String path = ex.getRequestURI().getPath().replace("/react-dist/", "");
            serveStatic(ex, "react-dist/" + path);
        });

        
        server.createContext("/game-json", ex -> {
            String user = getLoggedInUser(ex);
            if (user == null) {
                ex.sendResponseHeaders(401, -1);
                return;
            }
        
            Board board = getBoardForUser(user);
            Map<String, Object> json = new HashMap<>();
            json.put("grid", board.getGrid());
            json.put("message", "");
            json.put("gameOver", false);
        
            sendJson(ex, json);
        });

        server.createContext("/game-move", ex -> {
        String user = getLoggedInUser(ex);
        if (user == null) {
            ex.sendResponseHeaders(401, -1);
            return;
        }

        Map<String, String> q = parseQuery(ex.getRequestURI().getQuery());
        int r = Integer.parseInt(q.get("row"));
        int c = Integer.parseInt(q.get("col"));

        Board board = getBoardForUser(user);
        GameLogic logic = new GameLogic();
        char current = computeCurrentPlayer(board);
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
        sendJson(ex, json);
        });
    }

    private static String getSessionToken(HttpExchange ex) {
        List<String> cookies = ex.getRequestHeaders().get("Cookie");
        if (cookies == null) return null;
        for (String header : cookies) {
            String[] parts = header.split(";");
            for (String p : parts) {
                String[] kv = p.trim().split("=", 2);
                if (kv.length == 2 && kv[0].equals("SESSION")) return kv[1];
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
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); } catch (Exception e) { e.printStackTrace(); } 
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
                try (FileWriter w = new FileWriter(f)) {  w.write("E,E,E\nE,E,E\nE,E,E"); } catch (Exception e) { e.printStackTrace(); } 
            }
        } catch (IOException ignored) { ignored.printStackTrace(); }
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

    private static void handleRoot(HttpExchange ex) throws IOException {
        String user = getLoggedInUser(ex);
        ex.getResponseHeaders().add("Location", user == null ? "/login" : "/game");
        ex.sendResponseHeaders(302, -1);
        ex.close();
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
            } else  sendResponse(ex, renderer.loginPage("Invalid credentials."));
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

private static void serveStatic(HttpExchange ex, String path) throws IOException {
    File file = new File("src/tictactoe/web/" + path);

    if (!file.exists() || file.isDirectory()) {
        ex.sendResponseHeaders(404, -1);
        return;
    }

    String mime = switch (path.substring(path.lastIndexOf('.') + 1)) {
        case "html" -> "text/html";
        case "css" -> "text/css";
        case "js" -> "application/javascript";
        case "png" -> "image/png";
        case "jpg", "jpeg" -> "image/jpeg";
        case "svg" -> "image/svg+xml";
        case "wav" -> "audio/wav";
        default -> "application/octet-stream";
    };

    ex.getResponseHeaders().set("Content-Type", mime);
    byte[] bytes = java.nio.file.Files.readAllBytes(file.toPath());
    ex.sendResponseHeaders(200, bytes.length);
    ex.getResponseBody().write(bytes);
    ex.close();
}
