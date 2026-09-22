package ariketa05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Arik_5 {
    public static void main(String[] args) {
        try {
            Process p = new ProcessBuilder("tasklist").start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            boolean running = false;
            while ((line = r.readLine()) != null) {
                if (line.toLowerCase().contains("notepad.exe")) {
                    running = true;
                    break;
                }
            }
            if (running) {
                new ProcessBuilder("taskkill", "/F", "/IM", "notepad.exe").start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
