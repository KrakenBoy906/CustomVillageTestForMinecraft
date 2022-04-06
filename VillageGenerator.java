package com.shourya.customvillage;

import com.shourya.customvillage.VillageGeneratorConfig;
import com.shourya.customvillage.generators.area.AreaAllocator;
import com.shourya.customvillage.generators.area.AreaContext;
import com.shourya.customvillage.generators.path.PathGenerator;
import com.shourya.customvillage.util.UtilCompat;

import java.util.Arrays;
import java.util.List;

public class VillageGenerator {

    public int[][] structureMap;
    public int[][] pathMap;
    public int[][] areaAllocationMap;
    public int[][] areaConcentrationMap;

    public int[] seed;

    public AreaAllocator areaAllocator;
    public List<AreaContext> areas;
    public PathGenerator pathGenerator;

    public VillageGeneratorConfig config;

    public int spanningWidth;
    public int spanningHeight;


    public VillageGenerator(int width, int height, String seedString) throws IllegalArgumentException {
        if (!seedString.matches("[0-9]+"))
            throw new IllegalArgumentException("seed cannot contain any special character");
        breakSeed(seedString);
        this.spanningWidth = width;
        this.spanningHeight = height;
    }

    public VillageGenerator(int width, int height, VillageGeneratorConfig config) throws IllegalArgumentException {
        this.spanningWidth = width;
        this.spanningHeight = height;
        this.config = config;
    }

    private void breakSeed(String seedString) {
        char[] tokens = seedString.toCharArray();
        seed = new int[tokens.length];
        for (int i = 0; i < tokens.length; i ++) seed[i] = Integer.parseInt(String.valueOf(tokens[i]));

        System.out.println("seed : " + Arrays.toString(seed));
    }

    public void processAll() {
        allocateArea();
        generatePaths();
    }

    public void allocateArea() {
        areaAllocator = new AreaAllocator(spanningWidth, spanningHeight, config.areaAllocationBlockDim, config.noOfAreas, config.areaAllocatorSeed);
        areaAllocator.process();
        areas = areaAllocator.getAreas();
        areaConcentrationMap = areaAllocator.getAreaBlockMap();
        areaAllocationMap = UtilCompat.scaleArr3(areaConcentrationMap, config.areaAllocationBlockDim, config.areaAllocationBlockDim);
    }

    public void generatePaths() {
        pathGenerator = new PathGenerator(this, spanningWidth, spanningHeight, config.pathGeneratorSeed);
        pathGenerator.progress();
        pathMap = pathGenerator.getPathMap();
    }
}
