package tictactoe;

import java.util.*;

public class SessionManager {

    private final Map<String, String> tokenToUser = new HashMap<>();

    public String createSession(String username) {
        String token = UUID.randomUUID().toString();
        tokenToUser.put(token, username);
        return token;
    }

    public String getUser(String token) {
        return tokenToUser.get(token);
    }

    public void destroy(String token) {
        tokenToUser.remove(token);
    }
}
