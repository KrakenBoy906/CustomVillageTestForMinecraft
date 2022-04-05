package com.shourya.customvillage;

import java.util.Random;

public class VillageGeneratorConfig {
    public int noOfAreas;
    public int areaAllocationBlockDim;
    public long areaAllocatorSeed;
    public long pathGeneratorSeed;

    public Random random;

    public VillageGeneratorConfig() {
        random = new Random();

        noOfAreas = 5;// random.nextInt(5) + 1;
        areaAllocationBlockDim = (random.nextInt(9) + 2) * 5;
        areaAllocatorSeed = random.nextInt(10);
        pathGeneratorSeed = random.nextInt(10);
    }
}
