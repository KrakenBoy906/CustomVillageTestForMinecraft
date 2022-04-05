package com.shourya.customvillage.datatypes;

import java.util.Random;

public class Vector2 {
    public int x;
    public int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2(Config c) {
        if (c == Config.ZERO) {
            new Vector2();
        }
        else if (c == Config.ONE) {
            new Vector2(1, 1);
        }
        else if (c == Config.RANDOM) {
            new Vector2(new Random().nextInt(), new Random().nextInt());
        }
    }

    public int getFlattenedValue() {
        return (int)Math.sqrt(x * x + y * y);
    }

    public double distance(Vector2 other) {
        int dx = x - other.x;
        int dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static enum Config {
        ZERO,
        ONE,
        RANDOM;
    }
}
