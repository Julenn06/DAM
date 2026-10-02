package ariketa02;

public class Idazle extends Thread {
    private boolean zenbakiakDira;

    public Idazle(boolean zenbakiakDira) {
        this.zenbakiakDira = zenbakiakDira;
    }

    @Override
    public void run() {
        try {
            if (zenbakiakDira) {
                for (int i = 1; i <= 30; i++) {
                    System.out.print(i + " ");
                    Thread.sleep(15);
                }
            } else {
                for (char c = 'a'; c <= 'z'; c++) {
                    System.out.print(c + " ");
                    Thread.sleep(15);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Idazlea eten da.");
        }
    }
}
