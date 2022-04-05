package com.shourya.customvillage.generators.path;


import com.shourya.customvillage.datatypes.Vector2f;

public class PathCreatorPoint {
    public int threshold;
    Vector2f position;
    ProgressType progressType;
    ProgressDirection progressDirection;
    int currProgress = 0;
    double progressSpeed = 0;

    double angle;

    boolean isStuckByBoundaryOrPath = false;

    public PathCreatorPoint(Vector2f initialPosition, ProgressType T, ProgressDirection D, double progressSpeed, int threshold) {
        this.position = new Vector2f(initialPosition);
        this.progressType = T;
        this.progressDirection = D;
        this.currProgress = 0;
        this.progressSpeed = progressSpeed;
        this.threshold = threshold;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void progressStep(int[][] pathMap) {

        if (progressDirection == ProgressDirection.EAST) {
            position.x+= progressSpeed;
            if (progressType == ProgressType.ANGULAR) {
                position.y += (angle / 45) * progressSpeed;
            }
        }
        else if (progressDirection == ProgressDirection.WEST) {
            position.x-= progressSpeed;
            if (progressType == ProgressType.ANGULAR) {
                position.y -= (angle / 45) * progressSpeed;
            }
        }
        else if (progressDirection == ProgressDirection.NORTH) {
            position.y-= progressSpeed;
            if (progressType == ProgressType.ANGULAR) {
                position.x -= (angle / 45) * progressSpeed;
            }
        }
        else {
            position.y+= progressSpeed;
            if (progressType == ProgressType.ANGULAR) {
                position.x += (angle / 45) * progressSpeed;
            }
        }

        if (position.x < 0 || position.x >= pathMap.length || position.y < 0 || position.y >= pathMap[0].length) {
            isStuckByBoundaryOrPath = true;
        }
        else {
            if (pathMap[(int)position.x][(int)position.y] != 0) {
                isStuckByBoundaryOrPath = true;
            }
            currProgress ++;
            pathMap[(int)position.x][(int)position.y] ++;
        }
    }

    public boolean isStuck() {
        return isStuckByBoundaryOrPath;
    }

    public void desc() {
        System.out.println("type : " + progressType.toString());
        System.out.println("direction : " + progressDirection.toString());
        System.out.println("---------------------------------");
    }

    public enum ProgressType {
        STRAIGHT,
        ARC,
        ANGULAR
    }

    public enum ProgressDirection {
        NORTH,
        EAST,
        WEST,
        SOUTH
    }
}
