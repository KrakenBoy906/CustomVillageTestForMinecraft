package com.shourya.customvillage.generators.structure.park;

import com.shourya.customvillage.datatypes.Bound;
import com.shourya.customvillage.generators.area.AreaContext;
import com.shourya.customvillage.generators.structure.Structure;

import java.util.Arrays;
import java.util.Random;

public class Park {
    public static final int MIN_PARK_DECORATION_SIZE = 4;
    AreaContext areaContext;
    ParkSymmetry parkSymmetry;
    public int[][] parkConcentrationMap;
    Bound bound;
    int widthExtend;
    int heightExtend;

    Random random;

    public Park(Bound within, int widthExtend, int heightExtend, AreaContext areaContext, ParkSymmetry parkSymmetry) {
        this.bound = within;
        this.widthExtend = widthExtend;
        this.heightExtend = heightExtend;
        this.parkSymmetry = parkSymmetry;
        this.areaContext = areaContext;
        this.random = new Random();
    }

    private void initMatrix() {
        parkConcentrationMap = new int[bound.width / MIN_PARK_DECORATION_SIZE][bound.height / MIN_PARK_DECORATION_SIZE];
        for (int i = 0; i < parkConcentrationMap.length; i ++) {
            Arrays.fill(parkConcentrationMap[i], 0);
        }

        if (parkSymmetry == ParkSymmetry.IRREGULAR) {
            for (int i = widthExtend / MIN_PARK_DECORATION_SIZE; i < (bound.width - widthExtend) / MIN_PARK_DECORATION_SIZE; i ++) {
                Arrays.fill(parkConcentrationMap[i], heightExtend / MIN_PARK_DECORATION_SIZE, (bound.height - heightExtend) / MIN_PARK_DECORATION_SIZE, 1);
            }

            int i = widthExtend / MIN_PARK_DECORATION_SIZE;
            while (i < (bound.width - widthExtend) / MIN_PARK_DECORATION_SIZE) {
                int distortionPeek = random.nextInt(widthExtend / MIN_PARK_DECORATION_SIZE - 1);
                double distortionConvergenceRate = random.nextInt(3) + 1;
                int j = 0;
                while (j < distortionPeek) {
                    for (int k = 0; k < j * distortionConvergenceRate; k ++) {
                        parkConcentrationMap[widthExtend / MIN_PARK_DECORATION_SIZE - k][i + j] = 1;
                        parkConcentrationMap[(bound.width - widthExtend) / MIN_PARK_DECORATION_SIZE + k][i + j] = 1;
                    }
                    j ++;
                    //distortionConvergenceRate -= distortionPeek / 5;
                }
                i = i + j;
            }
        }
    }

    public void generate() {
        initMatrix();
    }

    public enum ParkSymmetry {
        RECTANGULAR,
        ROUNDED_RECTANGULAR,
        CIRCULAR,
        IRREGULAR
    }

    public enum ParkType {
        WATER,
        AMUSEMENT,
        ZOO
    }
}
