package dev.snake;

import java.io.IOException;
import java.nio.charset.Charset;

import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class IOControl {
    private final Terminal terminal;
    private final Attributes attributes;

    public IOControl() throws IOException, InterruptedException {
        this.terminal = TerminalBuilder.builder()
            .system(true)
            .encoding(Charset.forName("UTF-8"))
            .build();

        this.attributes = this.terminal.enterRawMode();
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
        return false;
    }

    public char input() {
        /* implement me */
        return 0;
    }

    public void close() {
        /* implement me */
        this.terminal.setAttributes(this.attributes);
    }
}
