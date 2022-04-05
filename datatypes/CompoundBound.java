package com.shourya.customvillage.datatypes;

import java.util.ArrayList;
import java.util.List;

public class CompoundBound {
    List<Bound> bounds;

    public CompoundBound() {
        bounds = new ArrayList<>();
    }

    public boolean intersects(CompoundBound other) {
        for (Bound b1 : bounds) {
            for (Bound b2 : other.bounds) {
                if (b1.intersects(b2))
                    return true;
            }
        }
        return false;
    }
}
