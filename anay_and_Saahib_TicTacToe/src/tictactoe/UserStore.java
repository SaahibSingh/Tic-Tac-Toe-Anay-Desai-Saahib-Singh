package tictactoe;

import java.io.*;
import java.util.*;

public class UserStore {

    private static final String USERS_FILE = "src/tictactoe/users.db";
    private final Map<String, User> users = new HashMap<>();

    public UserStore() {
        load();
    }

    private void load() {
        File f = new File(USERS_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    users.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (IOException ignored) {}
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users.values()) {
                pw.println(u.getUsername() + ":" + u.getPasswordHash());
            }
        } catch (IOException ignored) {}
    }

    public synchronized boolean addUser(String username, String passwordHash) {
        if (users.containsKey(username)) return false;
        users.put(username, new User(username, passwordHash));
        save();
        return true;
    }

    public User getUser(String username) {
        return users.get(username);
    }
}
