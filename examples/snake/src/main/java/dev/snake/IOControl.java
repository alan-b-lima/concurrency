package dev.snake;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class IOControl {
    private final Terminal terminal;
    private final Attributes attributes;
    private final Thread thread;

    private boolean hasInput;
    private char input;

    private ReentrantLock lock;

    private boolean started;
    private Condition condition;

    public IOControl() throws IOException, InterruptedException {
        this.terminal = TerminalBuilder.builder()
            .system(true)
            .encoding(Charset.forName("UTF-8"))
            .build();

        this.attributes = this.terminal.enterRawMode();

        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();

        this.thread = new Thread(() -> this.routine());
        this.thread.setDaemon(true);
        this.thread.start();

        this.lock.lock();
        try {
            while (!this.started) {
                this.condition.await();
            }
        } finally {
            this.lock.unlock();
        }
    }

    public int getWidth() {
        return this.terminal.getWidth();
    }

    public int getHeight() {
        return this.terminal.getHeight();
    }

    public boolean hasInput() {
        this.lock.lock();
        try {
            return this.hasInput;
        } finally {
            this.lock.unlock();
        }
    }

    public char input() {
        this.lock.lock();
        try {
            if (!this.hasInput) {
                throw new RuntimeException("no input");
            }

            this.hasInput = false;
            return this.input;
        } finally {
            this.lock.unlock();
        }
    }

    private void routine() {
        Reader reader = this.terminal.reader();

        this.lock.lock();
        try {
            this.started = true;
            this.condition.signal();
        } finally {
            this.lock.unlock();
        }

        while (true) {
            try {
                int character = reader.read();

                this.lock.lock();
                try {
                    this.input = (char) character;
                    this.hasInput = true;
                } finally {
                    this.lock.unlock();
                }
            } catch (IOException ex) {
                continue;
            }
        }
    }

    public void close() {
        this.thread.interrupt();
        this.terminal.setAttributes(this.attributes);
    }
}
