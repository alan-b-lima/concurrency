package dev.snake;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class IOControl {
    private final Terminal terminal;

    public IOControl() throws IOException, InterruptedException {
        this.terminal = TerminalBuilder.builder().system(true).build();
        /* implement me */
    }

    public int getWidth() {
        return this.terminal.getWidth();
    }

    public int getHeight() {
        return this.terminal.getHeight();
    }

    public boolean hasInput() {
        /* implement me */
    }

    public char input() {
        /* implement me */
    }

    public void close() {
        /* implement me */
    }

    private void routine() {
        this.terminal.enterRawMode();
        /* implement me */
    }
}
