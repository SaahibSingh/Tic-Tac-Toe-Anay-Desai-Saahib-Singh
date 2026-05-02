package tictactoe;
import java.util.*; //Import
public class Matchmaking {
    private final Queue<String> queue = new LinkedList<>();
    public synchronized String join(String user) {
        if (!queue.isEmpty()) return queue.poll();
        queue.add(user);
        return null;
    }
}
