package tictactoe;
//Imports
import java.util.Base64;
import java.nio.file.*;

public class DecodeWav {
    public static void main(String[] args) throws Exception {
        String base64 = "PASTE_BASE64_HERE";
        byte[] data = Base64.getDecoder().decode(base64);
        Files.write(Path.of("click.wav"), data);
    }
}
