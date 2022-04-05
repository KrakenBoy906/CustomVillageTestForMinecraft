package com.shourya.customvillage;

import com.shourya.customvillage.VillageGeneratorConfig;
import com.shourya.customvillage.generators.area.AreaAllocator;
import com.shourya.customvillage.generators.area.AreaContext;
import com.shourya.customvillage.generators.path.PathGenerator;
import com.shourya.Utils;

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

    int width;
    int height;


    public VillageGenerator(int width, int height, String seedString) throws IllegalArgumentException {
        if (!seedString.matches("[0-9]+"))
            throw new IllegalArgumentException("seed cannot contain any special character");
        breakSeed(seedString);
        this.width = width;
        this.height = height;
    }

    public VillageGenerator(int width, int height, VillageGeneratorConfig config) throws IllegalArgumentException {
        this.width = width;
        this.height = height;
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
        areaAllocator = new AreaAllocator(width, height, config.areaAllocationBlockDim, config.noOfAreas, config.areaAllocatorSeed);
        areaAllocator.process();
        areas = areaAllocator.getAreas();
        areaConcentrationMap = areaAllocator.getAreaBlockMap();
        areaAllocationMap = Utils.scaleArr3(areaConcentrationMap, config.areaAllocationBlockDim, config.areaAllocationBlockDim);
    }

    public void generatePaths() {
        pathGenerator = new PathGenerator(this, width, height, config.pathGeneratorSeed);
        pathGenerator.progress();
        pathMap = pathGenerator.getPathMap();
    }
}
