package tictactoe;
public class User {
    private final String username, passwordHash, String avatar;
    public User(String username, String passwordHash, String avatar) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.avatar = avatar;
    }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getAvatar() { return avatar; }
}
