package com.shourya.customvillage.generators.structure.house;
import com.shourya.customvillage.datatypes.Bound;
import com.shourya.customvillage.datatypes.Vector2;
import com.shourya.customvillage.util.UtilCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HouseCreator
{
	Random random;
	HouseContext context;

	List<HouseModule> houseModules;

	private int initialHouseShapeMatrixDim = 5;
	private int finalHouseShapeMatrixXDim = 0;
	private int finalHouseShapeMatrixYDim = 0;
	public int[][] houseShapeMatrix;
	
	private int[][] moduleAllocationMatrix;

	private boolean xSymmetry = true;
	private boolean ySymmetry = true;
	
	public HouseCreator(HouseContext context) {
		this.context = context;
		houseModules = new ArrayList<>();
		random = new Random();
		xSymmetry = random.nextBoolean();
		if (xSymmetry)
			ySymmetry = random.nextBoolean();
		else
			ySymmetry = true;
			
		System.out.println("xSymmetry : " + xSymmetry + " " + "ySymmetry : " + ySymmetry);
		
		initialHouseShapeMatrixDim = 2 * random.nextInt(3) + 1;
		
		System.out.println("initialHouseShapeMatrixDim : " + initialHouseShapeMatrixDim);
	}
	
	public void process() {
		generateShapeMatrix();
        fixUnwantedShape();
		generateModulesFromShapeMatrix_1();
	}
	
	public void generateShapeMatrix() {
		int noOfOccupation = (initialHouseShapeMatrixDim * initialHouseShapeMatrixDim) / (random.nextInt(2) + 2) + 1;
		System.out.println("noOfOccupation : " + noOfOccupation);
		houseShapeMatrix = new int[initialHouseShapeMatrixDim][initialHouseShapeMatrixDim];
		for (int i = 0; i < initialHouseShapeMatrixDim; i ++) {
			houseShapeMatrix[i] = new int[initialHouseShapeMatrixDim];
			Arrays.fill(houseShapeMatrix[i], 0);
		}
		
		houseShapeMatrix[initialHouseShapeMatrixDim / 2][initialHouseShapeMatrixDim / 2] = 1;

		Bound limit = new Bound(new Vector2(0, 0) , new Vector2(initialHouseShapeMatrixDim, initialHouseShapeMatrixDim));
		
		int noOfOccupiedBlocks = 1;
		
		while (noOfOccupiedBlocks < noOfOccupation) {
			int initX = random.nextInt(initialHouseShapeMatrixDim);
			int initY = random.nextInt(initialHouseShapeMatrixDim);
			while (houseShapeMatrix[initX][initY] != 0) {
				System.out.println("initX : " + initX + ", " + "initY : " + initY + "; not eligible as houseMatrix value there is : " + houseShapeMatrix[initX][initY]);
				initX = random.nextInt(initialHouseShapeMatrixDim);
				initY = random.nextInt(initialHouseShapeMatrixDim);
			}
			Vector2 initPoint = new Vector2(initX, initY);
			System.out.println("randomly placed on matrix : " + initPoint);
			while (! isAdjacentToOccupiedShapeMatrixBlock(initPoint)) {
				int chosenCoordinate = random.nextInt(2);
				int chosenDirection = random.nextInt(3) - 1;

				if (chosenCoordinate == 0)
					initPoint.translateUptoLimit(chosenDirection, 0, limit);
				else
					initPoint.translateUptoLimit(0, chosenDirection, limit);
			}
			System.out.println("after settling the point for adjacency : " + initPoint);
			noOfOccupiedBlocks ++;
			manageAdjacencyDegree(initPoint, initialHouseShapeMatrixDim, initialHouseShapeMatrixDim);

			Vector2 tempPoint = new Vector2(initPoint);
				
			if (xSymmetry && initPoint.x != initialHouseShapeMatrixDim / 2) {
				System.out.println("taking care of x symmetry");
				tempPoint.x = initialHouseShapeMatrixDim - tempPoint.x - 1;
				manageAdjacencyDegree(tempPoint, initialHouseShapeMatrixDim, initialHouseShapeMatrixDim);
				noOfOccupiedBlocks ++;
			}
			tempPoint = new Vector2(initPoint);
			if (ySymmetry && initPoint.y != initialHouseShapeMatrixDim / 2) {
				System.out.println("taking care of ysymmetry");
				tempPoint.y = initialHouseShapeMatrixDim - initPoint.y - 1;
				manageAdjacencyDegree(tempPoint, initialHouseShapeMatrixDim, initialHouseShapeMatrixDim);
				noOfOccupiedBlocks ++;
			}
			if (xSymmetry && initPoint.x != initialHouseShapeMatrixDim / 2 && ySymmetry && initPoint.y != initialHouseShapeMatrixDim / 2) {
				System.out.println("taking care of both symmetry");
				tempPoint.x = initialHouseShapeMatrixDim - tempPoint.x - 1;
				manageAdjacencyDegree(tempPoint, initialHouseShapeMatrixDim, initialHouseShapeMatrixDim);
				noOfOccupiedBlocks ++;
			}
		}
		
		/*for (int i = 0; i < initialHouseShapeMatrixDim; i ++) {
			for (int j = 0; j < initialHouseShapeMatrixDim; j ++) {
				if (isBlockConcavePositioned(i, j)) {
					manageAdjacencyDegree(new Vector2(i, j));
				}
			}
		}*/
		if (initialHouseShapeMatrixDim != 1)
			houseShapeMatrix[initialHouseShapeMatrixDim / 2][initialHouseShapeMatrixDim / 2] --;
		System.out.println("final matrix : ");
		UtilCompat.printArray(houseShapeMatrix, System.out);
		System.out.println("after trimming");
		houseShapeMatrix = UtilCompat.trimArrayWithValue(houseShapeMatrix, 0);
		finalHouseShapeMatrixXDim = houseShapeMatrix.length;
		finalHouseShapeMatrixYDim = houseShapeMatrix[0].length;
        
        System.out.println("finalHouseShapeMatrixXDim : " + finalHouseShapeMatrixXDim);
        System.out.println("finalHouseShapeMatrixYDim : " + finalHouseShapeMatrixYDim);
        
		UtilCompat.printArray(houseShapeMatrix, System.out);
        moduleAllocationMatrix = UtilCompat.copyArray(houseShapeMatrix);
	}

	private boolean isAdjacentToOccupiedShapeMatrixBlock(Vector2 pos) {
		return ((pos.x > 0 && houseShapeMatrix[pos.x - 1][pos.y] > 0) || (pos.x < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[pos.x + 1][pos.y] > 0) || (pos.y > 0 && houseShapeMatrix[pos.x][pos.y - 1] > 0) || (pos.y < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[pos.x][pos.y + 1] > 0));
	}
    
    private void fixUnwantedShape() {
        int adjacencySides = 0;
        float adjacencySummation = 0;
        for (int i = 0; i < finalHouseShapeMatrixXDim; i ++) {
            for (int j = 0; j < finalHouseShapeMatrixYDim; j ++) {
                adjacencySides = 0;
                adjacencySummation = 0;
                if (houseShapeMatrix[i][j] == 0) {
                    if (i > 0 && houseShapeMatrix[i - 1][j] > 0) {
                        adjacencySides ++;
                        adjacencySummation += houseShapeMatrix[i - 1][j];
                    }
                    
                    if (i < finalHouseShapeMatrixXDim - 1 && houseShapeMatrix[i + 1][j] > 0) {
                        adjacencySides ++;
                        adjacencySummation += houseShapeMatrix[i + 1][j];
                    }
                    
                    if (j > 0 && houseShapeMatrix[i][j - 1] > 0) {
                        adjacencySides ++;
                        adjacencySummation += houseShapeMatrix[i][j - 1];
                    }
                    
                    if (j < finalHouseShapeMatrixYDim - 1 && houseShapeMatrix[i][j + 1] > 0) {
                        adjacencySides ++;
                        adjacencySummation += houseShapeMatrix[i][j + 1];
                    }
                }
                
                if (adjacencySides > 0 && adjacencySummation / adjacencySides <= 1.5f) {
                    System.out.println("found " + adjacencySides + "adjacent occupied blocks with total summation : " + adjacencySummation + " around " + new Vector2(i, j));
                    manageAdjacencyDegree(new Vector2(i, j), finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);
                    if (xSymmetry && i != finalHouseShapeMatrixXDim / 2)
                        manageAdjacencyDegree(new Vector2(finalHouseShapeMatrixXDim - i - 1, j), finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);
                    if (ySymmetry && j != finalHouseShapeMatrixYDim / 2)
                        manageAdjacencyDegree(new Vector2(i, finalHouseShapeMatrixYDim - j - 1), finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);
                    if (xSymmetry && i != finalHouseShapeMatrixXDim / 2 && ySymmetry && j != finalHouseShapeMatrixYDim)
                        manageAdjacencyDegree(new Vector2(finalHouseShapeMatrixXDim - i - 1, finalHouseShapeMatrixYDim - j - 1), finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);
                }
            }
        }
    }
	
	private void manageAdjacencyDegree(Vector2 pos, int matrixLength, int matrixWidth) {

		if (pos.x > 0 && houseShapeMatrix[pos.x - 1][pos.y] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x - 1][pos.y] ++;
		}
		if (pos.x < matrixLength - 1 && houseShapeMatrix[pos.x + 1][pos.y] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x + 1][pos.y] ++;
		}

		if (pos.y > 0 && houseShapeMatrix[pos.x][pos.y - 1] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x][pos.y - 1] ++;
		}
		if (pos.y < matrixWidth - 1 && houseShapeMatrix[pos.x][pos.y + 1] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x][pos.y + 1] ++;
		}

		UtilCompat.printArray(houseShapeMatrix, System.out);
		System.out.println("--------------------");
	}
	
	private boolean isBlockConcavePositioned(int i, int j) {
		return (i > 0 && i < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[i - 1][j] == 1 && houseShapeMatrix[i + 1][j] == 1)
				|| (j > 0 && j < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[i][j - 1] == 1 && houseShapeMatrix[i][j + 1] == 1);
	}

	private void generateModulesFromShapeMatrix_1() {
        Vector2 centerPoint = new Vector2(finalHouseShapeMatrixXDim / 2, finalHouseShapeMatrixYDim / 2);
        Bound shapeBox = new Bound(new Vector2(Vector2.Config.ZERO), new Vector2(finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim));
        int noOfShapeBlockAllocated = 0;
        
        boolean done = false;

        //while (noOfShapeBlockAllocated < finalHouseShapeMatrixXDim * finalHouseShapeMatrixYDim) {
        while (true) {
            
            done = true;

            Vector2 suitableGreatestValuePoint = new Vector2(Vector2.Config.ZERO);
            for (int i = 0; i < moduleAllocationMatrix.length; i++) {
                for (int j = 0; j < moduleAllocationMatrix[i].length; j++) {
                    if (moduleAllocationMatrix[i][j] == 0) {
                        continue;
                    }
                    else if (moduleAllocationMatrix[i][j] > moduleAllocationMatrix[suitableGreatestValuePoint.x][suitableGreatestValuePoint.y]) {
                        done = false;
                        suitableGreatestValuePoint = new Vector2(i, j);
                        System.out.println("modulation starting point changed to larger value at : " + suitableGreatestValuePoint);
                    }
                    else if (moduleAllocationMatrix[i][j] == moduleAllocationMatrix[suitableGreatestValuePoint.x][suitableGreatestValuePoint.y]) {
                        done = false;
                        Vector2 currPoint = new Vector2(i, j);
                        if (centerPoint.distance(suitableGreatestValuePoint) > centerPoint.distance(currPoint)) {
                            suitableGreatestValuePoint = new Vector2(currPoint);
                            System.out.println("modulation starting point changed because being nearer at : " + suitableGreatestValuePoint);
                        }
                    }
                }
            }
            
            if (done)
                break;
                
            System.out.println("starting point for modualation found at : " + suitableGreatestValuePoint);


            Vector2 secondPoint = null;
            HouseModule.Direction direction = HouseModule.Direction.LEFT;

            int tempSecondPointX;
            int tempSecondPointY;


            tempSecondPointX = suitableGreatestValuePoint.x - 1;
            tempSecondPointY = suitableGreatestValuePoint.y;
            
            if (suitableGreatestValuePoint.x > 0 && moduleAllocationMatrix[tempSecondPointX][tempSecondPointY] > 0) {
                secondPoint = new Vector2(tempSecondPointX, tempSecondPointY);
                System.out.println("second point selected as the left with value : " + secondPoint + " : " + moduleAllocationMatrix[secondPoint.x][secondPoint.y]);
                direction = HouseModule.Direction.LEFT;
            }

            tempSecondPointX = suitableGreatestValuePoint.x + 1;
            tempSecondPointY = suitableGreatestValuePoint.y;
            
            if ((suitableGreatestValuePoint.x < finalHouseShapeMatrixXDim - 1 && moduleAllocationMatrix[tempSecondPointX][tempSecondPointY] > 0) && (secondPoint == null || moduleAllocationMatrix[tempSecondPointX][tempSecondPointY] > moduleAllocationMatrix[secondPoint.x][secondPoint.y])) {
                secondPoint = new Vector2(tempSecondPointX, tempSecondPointY);
                System.out.println("second point selected as the right with value : " + secondPoint + " : " + moduleAllocationMatrix[secondPoint.x][secondPoint.y]);
                direction = HouseModule.Direction.RIGHT;
            }

			tempSecondPointX = suitableGreatestValuePoint.x;
			tempSecondPointY = suitableGreatestValuePoint.y - 1;
			
            if ((suitableGreatestValuePoint.y > 0 && moduleAllocationMatrix[tempSecondPointX][tempSecondPointY] > 0) && (secondPoint == null ||moduleAllocationMatrix[tempSecondPointX][tempSecondPointY] > moduleAllocationMatrix[secondPoint.x][secondPoint.y])) {
                secondPoint = new Vector2(tempSecondPointX, tempSecondPointY);
                System.out.println("second point selected as the upper with value : " + secondPoint + " : " + moduleAllocationMatrix[secondPoint.x][secondPoint.y]);
                direction = HouseModule.Direction.UP;
            }
			
			tempSecondPointX = suitableGreatestValuePoint.x;
			tempSecondPointY = suitableGreatestValuePoint.y + 1;
		
			if ((suitableGreatestValuePoint.y < finalHouseShapeMatrixYDim - 1 && moduleAllocationMatrix[tempSecondPointX][tempSecondPointY] > 0) && (secondPoint == null || moduleAllocationMatrix[tempSecondPointX][tempSecondPointY] > moduleAllocationMatrix[secondPoint.x][secondPoint.y])) {
                secondPoint = new Vector2(tempSecondPointX, tempSecondPointY);
                System.out.println("second point selected as the bottom with value : " + secondPoint + " : " + moduleAllocationMatrix[secondPoint.x][secondPoint.y]);
                direction = HouseModule.Direction.DOWN;
            }
            
            if (secondPoint == null)
                System.out.println("secondPoint is null");
               else
                System.out.println("second point positional value : " + moduleAllocationMatrix[secondPoint.x][secondPoint.y]);

            System.out.println("secondPoint = " + secondPoint);
            System.out.println("direction = " + direction);

            HouseModule baseModule = new HouseModule(suitableGreatestValuePoint, direction);

            System.out.println("before expanding : " + baseModule);

            baseModule.lengthExpand(0, 0, moduleAllocationMatrix, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);

            System.out.println("after length expanding : " + baseModule);

            baseModule.widthExpand(0, 0, moduleAllocationMatrix, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);

            System.out.println("after width expanding : " + baseModule);
            
            HouseModule xMirrorModule = null, yMirrorModule = null, xyMirrorModule;

            houseModules.add(baseModule);
            noOfShapeBlockAllocated += baseModule.length * baseModule.width;
            eliminateModuledBlocksFromShapeMatrix(baseModule);

            if (xSymmetry) {
                xMirrorModule = baseModule.mirror(true, false, moduleAllocationMatrix, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);
                System.out.println("x mirror : " + xMirrorModule);
                if (! xMirrorModule.equalsWithOrExceptDirection(baseModule, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim)) {
                    System.out.println("xMirror locations are zeroed");
                    houseModules.add(xMirrorModule);
                    noOfShapeBlockAllocated += xMirrorModule.length * xMirrorModule.width;
                    eliminateModuledBlocksFromShapeMatrix(xMirrorModule);
                }
            }

            if (ySymmetry) {
                yMirrorModule = baseModule.mirror(false, true, moduleAllocationMatrix, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);
                System.out.println("y mirror : " + yMirrorModule);
                if (! yMirrorModule.equalsWithOrExceptDirection(baseModule, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim)) {
                    System.out.println("yMirror locations are zeroed");
                    houseModules.add(yMirrorModule);
                    noOfShapeBlockAllocated += yMirrorModule.length * yMirrorModule.width;
                    eliminateModuledBlocksFromShapeMatrix(yMirrorModule);
                }
            }

            if (xSymmetry && ySymmetry) {
                xyMirrorModule = baseModule.mirror(true, true, moduleAllocationMatrix, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);
                System.out.println("xy mirror : " + xyMirrorModule);
                if (! xyMirrorModule.equalsWithOrExceptDirection(baseModule, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim)) {
                    System.out.println("xyMirror locations are zeroed");
                    houseModules.add(xyMirrorModule);
                    noOfShapeBlockAllocated += xyMirrorModule.length * xyMirrorModule.width;
                    eliminateModuledBlocksFromShapeMatrix(xyMirrorModule);
                }
            }
            
            System.out.println("now moduleAllocationMatrix : " );
            UtilCompat.printArray(moduleAllocationMatrix, System.out);
        }
	}

	private void generateModulesFromShapeMatrix_2() {

	}
	
	private void eliminateModuledBlocksFromShapeMatrix(HouseModule houseModule) {
		
		switch(houseModule.direction) {
			case LEFT : {
				for (int i = 0; i < houseModule.length; i ++)
					for (int j = 0; j < houseModule.width; j ++)
						moduleAllocationMatrix[houseModule.startPoint.x - i][houseModule.startPoint.y + j] = 0;
				break;
			}
			case RIGHT : {
				for (int i = 0; i < houseModule.length; i ++)
					for (int j = 0; j < houseModule.width; j ++)
						moduleAllocationMatrix[houseModule.startPoint.x + i][houseModule.startPoint.y + j] = 0;
				break;
			}
			case UP : {
				for (int i = 0; i < houseModule.length; i ++)
					for (int j = 0; j < houseModule.width; j ++)
						moduleAllocationMatrix[houseModule.startPoint.x + j][houseModule.startPoint.y - i] = 0;
				break;
			}
			case DOWN : {
				for (int i = 0; i < houseModule.length; i ++)
					for (int j = 0; j < houseModule.width; j ++)
						moduleAllocationMatrix[houseModule.startPoint.x + j][houseModule.startPoint.y + i] = 0;
				break;
			}
			default : break;
		}
	}
}
