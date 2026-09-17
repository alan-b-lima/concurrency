package dev.snake;

import java.io.IOException;

import dev.snake.game.Move;
import dev.snake.game.Game;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        IOControl io = new IOControl();
        Game game = new Game();

        game.setSeed(0);
        game.start(9, 10, 3);

        Main.loop(io, game);

        if (game.won()) {
            System.out.println("You Won!");
        } else {
            System.out.println("Game Over!");
        }
    }

    private static void loop(IOControl io, Game game) throws InterruptedException {
        Main.setUp();
        try {
            Main.print(io, game);

            while (game.running()) {
                Thread.sleep(200);

                Move move = Main.input(io);
                game.advance(move);
                Main.print(io, game);
            }
        } finally {
            Main.tearDown();
        }
    }

    private static Move input(IOControl io) {
        if (!io.hasInput()) {
            return null;
        }

        switch (io.input()) {
        case 'w', 'A':
            return Move.UP;
        case 's', 'B':
            return Move.DOWN;
        case 'a', 'D':
            return Move.LEFT;
        case 'd', 'C':
            return Move.RIGHT;
        }
        return null;
    }

    private static void setUp() {
        System.out.print("" + //
            "\033[?1049h" + // enter alternate screen buffer
            "\033[?25l" + // hide cursor
            "\033[2J" + // erase entire screen
            "\033[1;1H" // move cursor to top-left
        );
    }

    private static void tearDown() {
        System.out.print("" + //
            "\033[?1049l" + // leave alternate screen buffer
            "\033[?25h" // show cursor
        );
    }

    private static void print(IOControl io, Game game) {
        int width = io.getWidth();
        int height = io.getHeight();

        int offsetX = 1 + width / 2 - (game.getWidth() + 2) / 2;
        int offsetY = 1 + height / 2 - (game.getHeight() / 2 + 2) / 2;

        System.out.printf("\033[%d;%dH%s", offsetY, offsetX, game.toString());
    }
}
