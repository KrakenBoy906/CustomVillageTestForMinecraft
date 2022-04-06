package com.shourya.customvillage.generators.structure;

import com.shourya.customvillage.datatypes.Bound;
import com.shourya.customvillage.datatypes.CompoundBound;

public class LandmarkStructure extends Structure {
    CompoundBound area;
    LandmarkType landmarkType;

    public LandmarkStructure(Bound baseBound, int widthElasticity, int heightElasticity) {
        super(baseBound, widthElasticity, heightElasticity);
    }

    public void generate(LandmarkType type) {
        this.landmarkType = type;
        if (landmarkType == LandmarkType.VERY_SMALL_PARK) {

        }
    }

    public enum LandmarkType {
        BIG_TOUR_HOUSE,
        WATCH_TOWER,
        VERY_SMALL_PARK,
        SMALL_PARK,
        CAFETERIA,
        POSTER,
        STATUE,
        RANDOM_PATTERN_LANDMARK
    }
}
