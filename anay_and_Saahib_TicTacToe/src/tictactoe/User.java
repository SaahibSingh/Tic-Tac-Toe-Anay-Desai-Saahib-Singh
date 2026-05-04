package tictactoe;
public class User {
    private final String username, passwordHash;
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
}