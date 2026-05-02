package tictactoe;

//Imports
import java.io.*;
import java.util.*;

public class Leaderboard {
    private static final String FILE = "src/tictactoe/leaderboard.db";
    private final Map<String, int[]> stats = new HashMap<>();
    public Leaderboard() { load(); }

    private void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(":");
                if (p.length == 4)  stats.put(p[0], new int[]{ Integer.parseInt(p[1]), Integer.parseInt(p[2]), Integer.parseInt(p[3]) });
            }
        } catch (Exception ignored) { ignored.printStackTrace(); }
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (var e : stats.entrySet()) {
                int[] s = e.getValue();
                pw.println(e.getKey() + ":" + s[0] + ":" + s[1] + ":" + s[2]);
            }
        } catch (Exception ignored) { ignored.printStackTrace(); }
    }

    public synchronized void recordWin(String user) { stats.computeIfAbsent(user, k -> new int[3])[0]++; save(); }
    public synchronized void recordLoss(String user) { stats.computeIfAbsent(user, k -> new int[3])[1]++; save(); }
    public synchronized void recordDraw(String user) { stats.computeIfAbsent(user, k -> new int[3])[2]++; save(); }

    public List<String> getRankings() {
        List<String> list = new ArrayList<>();
        for (var e : stats.entrySet()) {
            int[] s = e.getValue();
            list.add(e.getKey() + " — W:" + s[0] + " L:" + s[1] + " D:" + s[2]);
        }
        list.sort((a, b) -> b.compareTo(a));
        return list;
    }
}
