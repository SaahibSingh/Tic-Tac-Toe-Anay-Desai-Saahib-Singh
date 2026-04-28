package tictactoe;

import java.security.MessageDigest;

public class AuthService {

    private final UserStore store;

    public AuthService(UserStore store) {
        this.store = store;
    }

    public boolean register(String username, String password) {
        String hash = hash(password);
        return store.addUser(username, hash);
    }

    public boolean authenticate(String username, String password) {
        User u = store.getUser(username);
        if (u == null) return false;
        return u.getPasswordHash().equals(hash(password));
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte x : b) sb.append(String.format("%02x", x));
            return sb.toString();
        } catch (Exception e) {
            return input;
        }
    }
}
