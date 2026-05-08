package tictactoe;

import javax.sound.sampled.*;

public class Sound {

    public static void beepClick() { playTone(600, 80); }
    public static void beepWin() { playTone(900, 200); }
    public static void beepDraw() { playTone(400, 200); }
    public static void beepButton() { playTone(300, 60); }

    // ⭐ NEW INTRO CHIME
    public static void introChime() {
        playTone(700, 120);
        playTone(900, 120);
        playTone(1200, 200);
    }

    // ⭐ NEW WIN JINGLE
    public static void winJingle() {
        playTone(1200, 120);
        playTone(900, 120);
        playTone(1500, 200);
    }

    private static void playTone(int hz, int ms) {
        try {
            float sampleRate = 44100;
            byte[] buf = new byte[(int)(ms * sampleRate / 1000)];
            for (int i = 0; i < buf.length; i++) {
                double angle = i / (sampleRate / hz) * 2.0 * Math.PI;
                buf[i] = (byte)(Math.sin(angle) * 127);
            }

            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            Clip clip = AudioSystem.getClip();
            clip.open(af, buf, 0, buf.length);
            clip.start();
        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
        }
    }
}
