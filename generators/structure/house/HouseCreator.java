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
			manageAdjacencyDegree(initPoint);

			Vector2 tempPoint = new Vector2(initPoint);
				
			if (xSymmetry && initPoint.x != initialHouseShapeMatrixDim / 2) {
				System.out.println("taking care of x symmetry");
				tempPoint.x = initialHouseShapeMatrixDim - tempPoint.x - 1;
				manageAdjacencyDegree(tempPoint);
				noOfOccupiedBlocks ++;
			}
			tempPoint = new Vector2(initPoint);
			if (ySymmetry && initPoint.y != initialHouseShapeMatrixDim / 2) {
				System.out.println("taking care of ysymmetry");
				tempPoint.y = initialHouseShapeMatrixDim - initPoint.y - 1;
				manageAdjacencyDegree(tempPoint);
				noOfOccupiedBlocks ++;
			}
			if (xSymmetry && initPoint.x != initialHouseShapeMatrixDim / 2 && ySymmetry && initPoint.y != initialHouseShapeMatrixDim / 2) {
				System.out.println("taking care of both symmetry");
				tempPoint.x = initialHouseShapeMatrixDim - tempPoint.x - 1;
				manageAdjacencyDegree(tempPoint);
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
		UtilCompat.printArray(houseShapeMatrix, System.out);
	}

	private boolean isAdjacentToOccupiedShapeMatrixBlock(Vector2 pos) {
		return ((pos.x > 0 && houseShapeMatrix[pos.x - 1][pos.y] > 0) || (pos.x < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[pos.x + 1][pos.y] > 0) || (pos.y > 0 && houseShapeMatrix[pos.x][pos.y - 1] > 0) || (pos.y < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[pos.x][pos.y + 1] > 0));
	}
	
	private void manageAdjacencyDegree(Vector2 pos) {

		if (pos.x > 0 && houseShapeMatrix[pos.x - 1][pos.y] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x - 1][pos.y] ++;
		}
		if (pos.x < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[pos.x + 1][pos.y] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x + 1][pos.y] ++;
		}

		if (pos.y > 0 && houseShapeMatrix[pos.x][pos.y - 1] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x][pos.y - 1] ++;
		}
		if (pos.y < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[pos.x][pos.y + 1] > 0) {
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
		moduleAllocationMatrix = UtilCompat.createArr(finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim, 0);
		Bound shapeBox = new Bound(new Vector2(Vector2.Config.ZERO), new Vector2(finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim));
		Vector2 suitableGreatestValuePoint = new Vector2(Vector2.Config.ZERO);
		Vector2 centerPoint = new Vector2(finalHouseShapeMatrixXDim / 2, finalHouseShapeMatrixYDim / 2);
		for (int i = 0; i < houseShapeMatrix.length; i++) {
			for (int j = 0; j < houseShapeMatrix[i].length; j++) {
				if (houseShapeMatrix[i][j] > houseShapeMatrix[suitableGreatestValuePoint.x][suitableGreatestValuePoint.y]) {
					suitableGreatestValuePoint = new Vector2(i, j);
					System.out.println("modulation starting point changed to larger value at : " + suitableGreatestValuePoint);
				} else if (houseShapeMatrix[i][j] == houseShapeMatrix[suitableGreatestValuePoint.x][suitableGreatestValuePoint.y]) {
					Vector2 currPoint = new Vector2(i, j);
					if (centerPoint.distance(suitableGreatestValuePoint) > centerPoint.distance(currPoint)) {
						suitableGreatestValuePoint = new Vector2(currPoint);
						System.out.println("modulation starting point changed because being nearer at : " + suitableGreatestValuePoint);
					}
				}
			}
		}
		System.out.println("starting point for modualation found at : " + suitableGreatestValuePoint);

		
		Vector2 secondPoint = null;
		HouseModule.Direction direction = null;
		
		int tempSecondPointX;
		int tempSecondPointY;

		
		if (suitableGreatestValuePoint.x > 0) {
			tempSecondPointX = suitableGreatestValuePoint.x - 1;
			tempSecondPointY = suitableGreatestValuePoint.y;
			secondPoint = new Vector2(tempSecondPointX, tempSecondPointY);
			direction = HouseModule.Direction.LEFT;
		}
		
		if (suitableGreatestValuePoint.x < finalHouseShapeMatrixXDim - 1) {
			tempSecondPointX = suitableGreatestValuePoint.x + 1;
			tempSecondPointY = suitableGreatestValuePoint.y;
			if (secondPoint == null || houseShapeMatrix[tempSecondPointX][tempSecondPointY] > houseShapeMatrix[secondPoint.x][secondPoint.y]) {
				secondPoint = new Vector2(tempSecondPointX, tempSecondPointY);
				direction = HouseModule.Direction.RIGHT;
			}
		}

		if (suitableGreatestValuePoint.y > 0) {
			tempSecondPointX = suitableGreatestValuePoint.x;
			tempSecondPointY = suitableGreatestValuePoint.y - 1;
			if (secondPoint == null || houseShapeMatrix[tempSecondPointX][tempSecondPointY] > houseShapeMatrix[secondPoint.x][secondPoint.y]) {
				secondPoint = new Vector2(tempSecondPointX, tempSecondPointY);
				direction = HouseModule.Direction.UP;
			}
		}

		if (suitableGreatestValuePoint.y < finalHouseShapeMatrixYDim - 1) {
			tempSecondPointX = suitableGreatestValuePoint.x;
			tempSecondPointY = suitableGreatestValuePoint.y + 1;
			if (secondPoint == null || houseShapeMatrix[tempSecondPointX][tempSecondPointY] > houseShapeMatrix[secondPoint.x][secondPoint.y]) {
				secondPoint = new Vector2(tempSecondPointX, tempSecondPointY);
				direction = HouseModule.Direction.DOWN;
			}
		}

		System.out.println("secondPoint = " + secondPoint);
		System.out.println("direction = " + direction);
		
		HouseModule mainModule = new HouseModule(suitableGreatestValuePoint, direction);
		
		System.out.println("before expanding : " + mainModule);
		
		mainModule.lengthExpand(0, 0, houseShapeMatrix, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);
		
		System.out.println("after expanding : " + mainModule);
		
		houseModules.add(mainModule);
		
		HouseModule mirrorModule = mainModule.mirror(xSymmetry, ySymmetry, houseShapeMatrix, finalHouseShapeMatrixXDim, finalHouseShapeMatrixYDim);
		
		System.out.println("mirror module : " + mirrorModule);
		
		houseModules.add(mirrorModule);
	}

	private void generateModulesFromShapeMatrix_2() {

	}
}
