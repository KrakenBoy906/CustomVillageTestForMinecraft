package com.shourya.customvillage.datatypes;

import java.util.Random;

public class Vector2f {
    public double x;
    public double y;

    public Vector2f(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2f(Vector2f other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2f(Config c) {
        if (c == Config.ZERO) {
            new Vector2f();
        }
        else if (c == Config.ONE) {
            new Vector2f(1, 1);
        }
        else if (c == Config.RANDOM) {
            new Vector2f(new Random().nextDouble(), new Random().nextDouble());
        }
    }

    public double getFlattenedValue() {
        return (double)Math.sqrt(x * x + y * y);
    }

    public double distance(Vector2f other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public static enum Config {
        ZERO,
        ONE,
        RANDOM;
    }
}
