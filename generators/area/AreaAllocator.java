package com.shourya.customvillage.generators.area;

import com.shourya.customvillage.datatypes.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AreaAllocator {
    private int areaBlockMapWidth;
    private int areaBlockMapHeight;
    private int areaBlockDim;
    private int[][] areaBlockMap;

    private int noOfAreas;
    private List<AreaContext> areas;

    private Random random;

    private Canvas c;

    public AreaAllocator(int areaAllocationWidth, int areaAllocationHeight, int areaBlockDim, int noOfAreas, long seed) throws IllegalArgumentException
    {
        if (areaAllocationWidth <= 0 && areaAllocationHeight <= 0 && areaBlockDim <= 0 && noOfAreas <= 0)
            throw new IllegalArgumentException("constructor must be passed with non zero parameters");

        random = new Random(seed);
        this.areaBlockMapWidth = (int) Math.ceil((double)areaAllocationWidth / areaBlockDim);
        this.areaBlockMapHeight = (int) Math.ceil((double)areaAllocationHeight / areaBlockDim);
        this.areaBlockDim = areaBlockDim;

        this.noOfAreas = noOfAreas;

        this.areaBlockMap = new int[areaBlockMapWidth][areaBlockMapHeight];

        for (int i = 0; i < areaBlockMapWidth; i ++) {
            areaBlockMap[i] = new int[areaBlockMapHeight];
            for (int j = 0; j < areaBlockMapHeight; j ++) {
                areaBlockMap[i][j] = 0;
            }
        }

        areas = new ArrayList<>();
    }

    public void process() {
        int noOfUnOccupiedAreaBlocksRemaining = areaBlockMapWidth * areaBlockMapHeight;

        for (int i = 0 ; i < noOfAreas; i ++) {
            AreaContext area = new AreaContext(i + 1);

            Vector2 cp = new Vector2(random.nextInt(areaBlockMapWidth), random.nextInt(areaBlockMapHeight));

            if (areaBlockMap[cp.x][cp.y] == 0) {
                noOfUnOccupiedAreaBlocksRemaining --;
            }

            areaBlockMap[cp.x][cp.y] = i + 1;
            area.expandableEdgeAreaBlockPosList.add(cp);
            areas.add(area);
        }

        while (noOfUnOccupiedAreaBlocksRemaining > 0) {
            int randAreaIndex = random.nextInt(noOfAreas);
            AreaContext selectedArea = areas.get(randAreaIndex);
            if (selectedArea.expandableEdgeAreaBlockPosList.isEmpty()) {
                continue;
            }
            int randEdgeAreaBlockIndex = random.nextInt(selectedArea.expandableEdgeAreaBlockPosList.size());

            Vector2 currVector2 = selectedArea.expandableEdgeAreaBlockPosList.get(randEdgeAreaBlockIndex);

            boolean isNextToAnotherTypeArea = false;

            for (int i = currVector2.x - 1; i <= currVector2.x + 1; i ++ ) {
                for (int j = currVector2.y - 1; j <= currVector2.y + 1; j ++ ) {
                    try {
                        if (areaBlockMap[i][j] == 0) {
                            areaBlockMap[i][j] = selectedArea.getTagInfo();
                            areas.get(randAreaIndex).expandableEdgeAreaBlockPosList.add(new Vector2(i, j));
                            noOfUnOccupiedAreaBlocksRemaining --;
                        }
                        else if (i != currVector2.x && j != currVector2.y && areaBlockMap[i][j] != selectedArea.getTagInfo()) {
                            isNextToAnotherTypeArea = true;
                        }
                    }
                    catch (IndexOutOfBoundsException e) {
                        isNextToAnotherTypeArea = true;
                    }
                }
            }

            areas.get(randAreaIndex).expandableEdgeAreaBlockPosList.remove(randEdgeAreaBlockIndex);

            if (isNextToAnotherTypeArea)
                areas.get(randAreaIndex).nonExpandableEdgeAreaBlockPosList.add(currVector2);
        }
    }

    public List<AreaContext> getAreas() {
        return areas;
    }

    public int getAreaBlockMapWidth() {
        return areaBlockMapWidth;
    }

    public int getAreaBlockMapHeight() {
        return areaBlockMapHeight;
    }

    public int getAreaBlockDim() {
        return areaBlockDim;
    }

    public int[][] getAreaBlockMap() {
        return areaBlockMap;
    }

    public int[][] getBlockMap() {
        int[][] blockMap = new int[areaBlockMapWidth * areaBlockDim][areaBlockMapHeight * areaBlockDim];

        for (int i = 0; i < areaBlockMapWidth * areaBlockDim; i ++) {
            blockMap[i] = new int[areaBlockMapHeight * areaBlockDim];
        }

        for (int i = 0; i < areaBlockMapWidth; i ++) {
            for (int j = 0; j < areaBlockMapHeight; j ++) {

                for (int k = 0; k < areaBlockDim; k++) {
                    for (int l = 0; l < areaBlockDim; l++) {
                        blockMap[i * areaBlockDim + k][j * areaBlockDim + l] = areaBlockMap[i][j];
                    }
                }
            }
        }

        return blockMap;
    }
}
