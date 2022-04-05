package com.shourya.customvillage.generators.path;


import com.shourya.customvillage.VillageGenerator;
import com.shourya.customvillage.datatypes.Vector2f;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PathGenerator {
    int[][] pathMap;
    int pathMapWidth;
    int pathMapHeight;

    List<PathCreatorPoint> progressingPoints;
    Random random;

    Canvas canvas;

    VillageGenerator parent;


    public PathGenerator(VillageGenerator parent, int pathMapWidth, int pathMapHeight, long pathGeneratorSeed) {
        random = new Random(pathGeneratorSeed);
        this.parent = parent;
        this.pathMapWidth = pathMapWidth;
        this.pathMapHeight = pathMapHeight;

        progressingPoints = new LinkedList<>();

        initPathMap();
        initiatePoints();
    }

    private void initPathMap() {
        pathMap = new int[pathMapWidth][pathMapHeight];
        for (int i = 0; i < pathMapWidth; i ++) {
            pathMap[i] = new int[pathMapHeight];
            for (int j = 0; j < pathMapHeight; j ++) {
                pathMap[i][j] = 0;
            }
        }
    }

    private void initiatePoints() {
        Vector2f initPoint = new Vector2f(random.nextInt(pathMapWidth), random.nextInt(pathMapHeight));

        pathMap[(int)initPoint.x][(int)initPoint.y] = 4;
        progressingPoints.add(new PathCreatorPoint(initPoint, PathCreatorPoint.ProgressType.STRAIGHT, PathCreatorPoint.ProgressDirection.NORTH, 1, 50));
        progressingPoints.add(new PathCreatorPoint(initPoint, PathCreatorPoint.ProgressType.STRAIGHT, PathCreatorPoint.ProgressDirection.EAST, 1, 50));
        progressingPoints.add(new PathCreatorPoint(initPoint, PathCreatorPoint.ProgressType.STRAIGHT, PathCreatorPoint.ProgressDirection.WEST, 1, 50));
        progressingPoints.add(new PathCreatorPoint(initPoint, PathCreatorPoint.ProgressType.STRAIGHT, PathCreatorPoint.ProgressDirection.SOUTH, 1, 50));
    }

    public void bindCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public int[][] getPathMap() {
        return pathMap;
    }

    public void progress() {
        while (! progressingPoints.isEmpty()) {
            int j = 0;
            while (j < progressingPoints.size()) {

                PathCreatorPoint p = progressingPoints.get(j);


                if (p.isStuck()) {
                    progressingPoints.remove(j);
                    continue;
                }

                int thresHoldByAreaType;
                if (p.progressType == PathCreatorPoint.ProgressType.ANGULAR)
                    thresHoldByAreaType = parent.areas.get(parent.areaAllocationMap[(int)p.position.y][(int)p.position.x] - 1).getTagInfo() * 10;
                else
                    thresHoldByAreaType = parent.areas.get(parent.areaAllocationMap[(int)p.position.y][(int)p.position.x] - 1).getTagInfo() * 20;

                int divertOrNot = random.nextInt(thresHoldByAreaType);

                if (divertOrNot == 1) {
                    int noOfNewPoints = 0;
                    int directionToExpand = Arrays.asList(PathCreatorPoint.ProgressDirection.values()).indexOf(p.progressDirection);
                    for (int k = 0; k < 3; k ++) {
                        directionToExpand = (directionToExpand + 1) % 4;
                        boolean createOrNot = random.nextBoolean();
                        //boolean createOrNot = true;
                        if (createOrNot) {
                            noOfNewPoints ++;
                            if (p.progressType == PathCreatorPoint.ProgressType.STRAIGHT && random.nextInt(10) == 5) {
                                PathCreatorPoint p1 = new PathCreatorPoint(p.position, PathCreatorPoint.ProgressType.ANGULAR, PathCreatorPoint.ProgressDirection.values()[directionToExpand], 1, 20);
                                p1.setAngle((random.nextInt(3) - 1) * random.nextInt(10));
                                progressingPoints.add(p1);

                            }
                            else progressingPoints.add(new PathCreatorPoint(p.position, PathCreatorPoint.ProgressType.STRAIGHT, PathCreatorPoint.ProgressDirection.values()[directionToExpand], 1, thresHoldByAreaType
                            ));
                        }
                    }
                    pathMap[(int)p.position.x][(int)p.position.y] += noOfNewPoints;

                    if (noOfNewPoints < 0)
                        progressingPoints.remove(j);
                }
                else {
                    p.progressStep(pathMap);
                }
                j ++;
            }
        }
    }
}
