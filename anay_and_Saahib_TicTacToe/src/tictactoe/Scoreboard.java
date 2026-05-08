package tictactoe;
public class Scoreboard {
    private int p1Wins = 0;
    private int p2Wins = 0;
    private int draws = 0;
    public void addWinForPlayer1() { p1Wins++; }
    public void addWinForPlayer2() { p2Wins++; }
    public void addDraw() { draws++; }
    public String getScoreboard(String p1, String p2) {
        return "\n=== Scoreboard ===\n" +
               p1 + " (X) wins: " + p1Wins + "\n" +
               p2 + " (O) wins: " + p2Wins + "\n" +
               "Draws: " + draws + "\n";
    }
}