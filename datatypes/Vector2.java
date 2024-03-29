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
            this.x = 0;
			this.y = 0;
        }
        else if (c == Config.ONE) {
            this.x = 1;
			this.y = 1;
        }
        else if (c == Config.RANDOM) {
            this.x = new Random().nextInt();
			this.y = new Random().nextInt();
        }
    }
	
	public void translate(int tx, int ty) {
		this.x += tx;
		this.y += ty;
	}
	
	public void translate(int tx, int ty, Bound limit) throws IllegalArgumentException {
		if (x + tx >= limit.p2.x || x + tx < limit.p1.x || y + ty >= limit.p2.y || y + ty < limit.p1.y) {
			throw new IllegalArgumentException("the translation of point goes outside the bound region");
		}
		translate(tx, ty);
	}
	
	public void translateUptoLimit(int tx, int ty, Bound limit) {
		if (x + tx >= limit.p2.x)
			x = limit.p2.x - 1;
		else if (x + tx < limit.p1.x)
			x = limit.p1.x;
		else
			translate(tx, 0);
			
		if (y + ty >= limit.p2.y)
			y = limit.p2.y - 1;
		else if (y + ty < limit.p1.y)
			y = limit.p1.y;
		else
			translate(0, ty);
	}

    public int getFlattenedValue() {
        return (int)Math.sqrt(x * x + y * y);
    }

    public double distance(Vector2 other) {
        int dx = x - other.x;
        int dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
	
	public boolean equals(Vector2 other) {
		return x == other.x && y == other.y;
	}

    @Override
    public String toString() {
        return "x : " + x + ", y : " + y;
    }

    @Override
    protected Vector2 clone() throws CloneNotSupportedException {
        return new Vector2(this);
    }

    public static enum Config {
        ZERO,
        ONE,
        RANDOM;
    }
}
