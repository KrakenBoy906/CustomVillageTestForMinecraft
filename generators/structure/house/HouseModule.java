package com.shourya.customvillage.generators.structure.house;

public class HouseModule {
    public int startIndex = 0;
    public int length = 0;
    public int width = 0;
    Direction direction = Direction.UP;
    
    public HouseModule(int startIndex, int length, int width, Direction direction) {
        this.startIndex = startIndex;
        this.length = length;
        this.width = width;
        this.direction = direction;
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }
}
