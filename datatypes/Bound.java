package com.shourya.customvillage.datatypes;

public class Bound {
    public Vector2 center;
    public int width;
    public int height;
    public int area;
    public Vector2 p1;
    public Vector2 p2;

    public Bound(Vector2 c, int w, int h) {
        center = c;
        width = w;
        height = h;
        p1 = new Vector2(c.x - w / 2, c.y - h / 2);
        p2 = new Vector2(c.x + w / 2, c.y + h / 2);
        area = calculateArea();
    }

    public Bound(Vector2 p1, Vector2 p2) {
        this.p1 = p1;
        this.p2 = p2;
        center = new Vector2((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        width = Math.abs(p2.x - p1.x);
        height = Math.abs(p2.x - p1.y);
        area = calculateArea();
    }

    public boolean intersects(Bound other) {
        return (Math.abs(center.x - other.center.x) < (width + other.width) / 2) && (Math.abs(center.y - other.center.y) < (height + other.height) / 2);
    }

    public boolean touches(Bound other) {
        return (Math.abs(center.x - other.center.x) < (width + other.width) / 2) && (Math.abs(center.y - other.center.y) < (height + other.height) / 2);
    }

    public Vector2 distance(Bound other) {
        return new Vector2(center.x - other.center.x, center.y - other.center.y);
    }

    public int gapX(Bound other) {
        if (center.x > other.center.x)
            return other.p2.x - p1.x;
        return p1.x - other.p2.x;
    }

    public int gapY(Bound other) {
        if (center.y > other.center.y)
            return other.p2.y - p1.y;
        return p1.y - other.p2.y;
    }

    private int calculateArea()
    {
        return width * height;
    }

    public int getArea()
    {
        return area;
    }

    public void compress(int amount, int side)
    {
        if (side == SIDE.LEFT || side == SIDE.RIGHT) {
            if (amount >= width)
                return;
            if (side == SIDE.LEFT)
            {
                center.x += amount / 2;
                p1.x += amount;
            }
            else
            {
                center.x -= amount / 2;
                p2.x -= amount;
            }
            width -= amount;
        }

        if (side == SIDE.TOP || side == SIDE.BOTTOM) {
            if (amount >= height)
                return;
            if (side == SIDE.BOTTOM)
            {
                center.y -= amount / 2;
                p2.y -= amount;
            }
            else
            {
                center.y += amount / 2;
                p1.y += amount;
            }
            height -= amount;
        }

        area = calculateArea();
    }

    @Override
    public String toString()
    {
        return "center : " + center.x + ", " + center.y + "; width : " + width + ", height : " + height;
    }

    public static class SIDE
    {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
        public static final int TOP = 2;
        public static final int BOTTOM = 3;
    }
}
