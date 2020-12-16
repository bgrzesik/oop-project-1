package project1;

import java.io.PrintStream;

public class TerminalBackend {


    private static final String ESC = new String(new byte[]{0x1b, 0x5b});
    private static final String X11_MOUSE_REPORTING_ON = ESC + "?1000h";
    private static final String X11_MOUSE_REPORTING_OFF = ESC + "?1000l";
    private static final String CLEAR_SCREEN = ESC + "2J";

    private final PrintStream out = System.out;

    public void draw() {
        for (int i = 0; i < 20; i++) {
            out.print(CLEAR_SCREEN);
            out.printf("Well, hello there %d\n", i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        new TerminalBackend().draw();
    }

}
