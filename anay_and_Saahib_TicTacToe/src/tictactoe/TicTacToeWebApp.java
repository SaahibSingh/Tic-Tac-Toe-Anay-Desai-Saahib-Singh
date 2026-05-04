package tictactoe;

//Import
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;

public class TicTacToeWebApp {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // HTML pages
        server.createContext("/login", ex -> WebRenderer.serve(ex, "login.html"));
        server.createContext("/signup", ex -> WebRenderer.serve(ex, "signup.html"));
        server.createContext("/game", ex -> WebRenderer.serve(ex, "game.html"));
        server.createContext("/leaderboard", ex -> WebRenderer.serve(ex, "leaderboard.html"));
        server.createContext("/chat", ex -> WebRenderer.serve(ex, "chat.html"));
        server.createContext("/matchmaking", ex -> WebRenderer.serve(ex, "matchmaking.html"));

        // Static assets
        server.createContext("/styles.css", ex -> WebRenderer.serve(ex, "styles.css"));
        server.createContext("/neon.css", ex -> WebRenderer.serve(ex, "neon.css"));

        server.createContext("/sounds", ex -> {
            String path = ex.getRequestURI().getPath().replace("/sounds/", "");
            WebRenderer.serve(ex, "sounds/" + path);
        });

        server.createContext("/react-dist", ex -> {
            String path = ex.getRequestURI().getPath().replace("/react-dist/", "");
            WebRenderer.serve(ex, "react-dist/" + path);
        });

        // JSON API
        server.createContext("/game-json", GameAPI::handleGameState);
        server.createContext("/game-move", GameAPI::handleMove);
        server.createContext("/leaderboard-json", LeaderboardAPI::handleLeaderboard);
        server.createContext("/chat-json", ChatAPI::handleChatGet);
        server.createContext("/chat-post", ChatAPI::handleChatPost);
        server.createContext("/matchmaking-json", MatchmakingAPI::handleMatchmaking);

        System.out.println("Server running on http://localhost:8080");
        server.start();
    }
}
