package tictactoe;
public class User {
    private final String username;
    private final String passwordHash;
    private final String avatar;
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    
    public String getAvatar() { return avatar; }
}
