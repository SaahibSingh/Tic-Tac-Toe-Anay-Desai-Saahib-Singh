package tictactoe;

//Imports
import java.io.*;
import java.util.*;

public class Leaderboard {
    private Map<String, Integer> wins = new HashMap<>();
    private Map<String, Integer> games = new HashMap<>();

    private final String FILE_NAME = "leaderboard.txt";

    public Leaderboard() { load(); }
    public void recordWin(String player) {
        wins.put(player, wins.getOrDefault(player, 0) + 1);
        games.put(player, games.getOrDefault(player, 0) + 1);
        save();
    }

    public void recordLoss(String player) {
        games.put(player, games.getOrDefault(player, 0) + 1);
        save();
    }

    public void recordDraw(String p1, String p2) {
        games.put(p1, games.getOrDefault(p1, 0) + 1);
        games.put(p2, games.getOrDefault(p2, 0) + 1);
        save();
    }

    public String getLeaderboard() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== Leaderboard ===\n");
        List<String> players = new ArrayList<>(games.keySet());
        players.sort((a, b) -> wins.getOrDefault(b, 0) - wins.getOrDefault(a, 0));
        for (String p : players) {
            int w = wins.getOrDefault(p, 0);
            int g = games.getOrDefault(p, 0);
            double pct = g == 0 ? 0 : (w * 100.0 / g);

            sb.append(String.format("%s — Wins: %d | Games: %d | Win%%: %.1f\n",
                    p, w, g, pct));
        }

        return sb.toString();
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) { for (String p : games.keySet()) pw.println(p + "," + wins.getOrDefault(p, 0) + "," + games.getOrDefault(p, 0));
        } catch (Exception e) { System.out.println("Error saving leaderboard: " + e.getMessage()); }
    }

    public void reset() { wins.clear(); games.clear(); save(); }
    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String player = parts[0];
                int w = Integer.parseInt(parts[1]);
                int g = Integer.parseInt(parts[2]);
                wins.put(player, w);
                games.put(player, g);
            }
        } catch (Exception e) {}
    }
}