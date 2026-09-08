package dev.snake.game;

import java.security.InvalidParameterException;
import java.util.Random;

public class Game {
    private Random random;

    private Section[] field;
    private int width;
    private int height;

    private Move move;
    private Point head;
    private Point tail;
    private int size;

    private boolean running;
    private boolean won;

    public Game() {
        this.random = new Random();
    }

    public void setSeed(long seed) {
        this.random.setSeed(seed);
    }

    public void start(int width, int height, int startingSize) {
        if (width <= 0 || height <= 0) {
            throw new InvalidParameterException("Width and height must be positive");
        }
        if (startingSize <= 0) {
            throw new InvalidParameterException("Starting size must be positive");
        }
        if (startingSize >= width) {
            throw new InvalidParameterException("Starting size must be lesser than the width");
        }

        if ((height & 1) == 1) {
            height++;
        }

        this.width = width;
        this.height = height;

        this.running = true;
        this.won = false;

        this.field = new Section[width * height];
        for (int i = 0; i < this.field.length; i++) {
            this.field[i] = Section.FIELD;
        }

        int middle = height / 2;

        this.move = Move.RIGHT;
        this.tail = new Point(1, middle);
        this.head = new Point(startingSize, middle);
        this.size = startingSize;

        for (int i = 0; i < startingSize - 1; i++) {
            this.set(1 + i, middle, Section.BODY_RIGHT);
        }
        this.set(startingSize, middle, Section.HEAD);

        this.putFruit();
    }

    public boolean running() {
        return this.running;
    }

    public boolean won() {
        return this.won;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void advance(Move move) {
        if (this.running == false) {
            return;
        }

        this.turn(move);

        Point next = new Point();
        next.add(this.head, Section.fromMove(this.move).vector());

        switch (this.at(next.x, next.y)) {
        case Section.OOW:
        case Section.HEAD:
        case Section.BODY_UP:
        case Section.BODY_DOWN:
        case Section.BODY_LEFT:
        case Section.BODY_RIGHT: {
            this.running = false;
            this.won = false;
            return;
        }

        case Section.FIELD: {
            this.advanceHead();
            this.advanceTail();
            break;
        }

        case Section.FRUIT: {
            this.advanceHead();
            this.putFruit();
            this.size++;

            if (this.size >= this.width * this.height) {
                this.running = false;
                this.won = true;
            }
            break;
        }
        }
    }

    private void turn(Move move) {
        if (move == null) {
            return;
        }

        switch (this.move) {
        case Move.UP, Move.DOWN:
            switch (move) {
            case Move.LEFT, Move.RIGHT:
                this.move = move;
            default:
                return;
            }

        case Move.LEFT, Move.RIGHT:
            switch (move) {
            case Move.UP, Move.DOWN:
                this.move = move;
            default:
                return;
            }
        }
    }

    private void advanceHead() {
        Section head = Section.fromMove(this.move);
        this.set(this.head.x, this.head.y, head);
        this.head.add(this.head, head.vector());
        this.set(this.head.x, this.head.y, Section.HEAD);
    }

    private void advanceTail() {
        Section tail = this.at(this.tail.x, this.tail.y);
        this.set(this.tail.x, this.tail.y, Section.FIELD);
        this.tail.add(this.tail, tail.vector());
    }

    private void putFruit() {
        int x = this.random.nextInt(0, this.width);
        int y = this.random.nextInt(0, this.height);

        while (this.at(x, y) != Section.FIELD) {
            x++;
            if (x >= this.width) {
                x = 0;
                y++;
            }
        }

        this.set(x, y, Section.FRUIT);
    }

    private Section at(int x, int y) {
        if (x < 0 || this.width <= x || y < 0 || this.height <= y) {
            return Section.OOW;
        }

        return this.field[y * this.width + x];
    }

    private void set(int x, int y, Section section) {
        if (x < 0 || this.width <= x || y < 0 || this.height <= y) {
            return;
        }

        this.field[y * this.width + x] = section;
    }

    public String toString() {
        if (this.running == false) {
            return "{}";
        }

        StringBuilder builder = new StringBuilder();

        String status = String.format("%d %dx%d", this.size, this.width, this.height);
        String newline = String.format("\033[%dD\033[1B", this.width + 2);

        builder.append(status);
        builder.append(String.format("\033[%dD\033[1B", status.length()));

        builder.append("┌");
        builder.repeat("─", this.width);
        builder.append("┐");

        for (int y = 0; y < this.height; y += 2) {
            builder.append(newline);
            builder.append("│");

            for (int x = 0; x < this.width; x++) {
                switch (this.at(x, y)) {
                case Section.OOW:
                case Section.FIELD:
                    builder.append("\033[40;");
                    break;

                case Section.HEAD:
                case Section.BODY_UP:
                case Section.BODY_DOWN:
                case Section.BODY_LEFT:
                case Section.BODY_RIGHT:
                    builder.append("\033[47;");
                    break;

                case Section.FRUIT:
                    builder.append("\033[41;");
                    break;
                }

                switch (this.at(x, y + 1)) {
                case Section.OOW:
                case Section.FIELD:
                    builder.append("30m▄");
                    break;

                case Section.HEAD:
                case Section.BODY_UP:
                case Section.BODY_DOWN:
                case Section.BODY_LEFT:
                case Section.BODY_RIGHT:
                    builder.append("37m▄");
                    break;

                case Section.FRUIT:
                    builder.append("31m▄");
                    break;
                }
            }

            builder.append("\033[m│");
        }

        builder.append(newline);
        builder.append("└");
        builder.repeat("─", this.width);
        builder.append("┘");

        return builder.toString();
    }
}

enum Section {
    OOW, FIELD, HEAD, BODY_UP, BODY_DOWN, BODY_LEFT, BODY_RIGHT, FRUIT;

    public static Section fromMove(Move move) {
        switch (move) {
        case Move.UP:
            return BODY_UP;
        case Move.DOWN:
            return BODY_DOWN;
        case Move.LEFT:
            return BODY_LEFT;
        case Move.RIGHT:
            return BODY_RIGHT;
        default:
            throw new IllegalStateException("Cannot get section of " + move);
        }
    }

    public boolean isBody() {
        switch (this) {
        case BODY_UP:
        case BODY_DOWN:
        case BODY_LEFT:
        case BODY_RIGHT:
            return true;

        default:
            return false;
        }
    }

    public Point vector() {
        switch (this) {
        case BODY_UP:
            return new Point(0, -1);
        case BODY_DOWN:
            return new Point(0, 1);
        case BODY_LEFT:
            return new Point(-1, 0);
        case BODY_RIGHT:
            return new Point(1, 0);
        default:
            throw new IllegalStateException("Cannot get vector of " + this);
        }
    }
}

class Point {
    public int x, y;

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(Point p0, Point p1) {
        this.x = p0.x + p1.x;
        this.y = p0.y + p1.y;
    }

    public String toString() {
        return "(%d, %d)".formatted(this.x, this.y);
    }
}
