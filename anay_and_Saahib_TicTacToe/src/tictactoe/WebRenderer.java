package tictactoe;

//Imports
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.nio.file.Files;

public class WebRenderer {
    private static final String WEB_ROOT = "src/tictactoe/web/";
    public static void serve(HttpExchange ex, String filePath) throws IOException {
        File file = new File(WEB_ROOT + filePath);
        if (!file.exists() || file.isDirectory()) { ex.sendResponseHeaders(404, -1); return; }
        String mime = getMimeType(filePath);
        ex.getResponseHeaders().set("Content-Type", mime);
        byte[] bytes = Files.readAllBytes(file.toPath());
        ex.sendResponseHeaders(200, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }

    private static String getMimeType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".svg")) return "image/svg+xml";
        if (path.endsWith(".wav")) return "audio/wav";
        return "application/octet-stream";
    }
}
