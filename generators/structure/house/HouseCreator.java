package com.shourya.customvillage.generators.structure.house;
import com.shourya.customvillage.datatypes.Bound;
import com.shourya.customvillage.datatypes.Vector2;
import com.shourya.customvillage.util.UtilCompat;

import java.util.Arrays;
import java.util.Random;

public class HouseCreator
{
	Random random;
	HouseContext context;
	
	private static final int initialHouseShapeMatrixDim = 5;
	private int finalHouseShapeMatrixXDim = 0;
	private int finalHouseShapeMatrixYDim = 0;
	public int[][] houseShapeMatrix;

	private boolean xSymmetry = true;
	private boolean ySymmetry = true;
	
	public HouseCreator(HouseContext context) {
		this.context = context;
		random = new Random();
		xSymmetry = random.nextBoolean();
		if (xSymmetry)
			ySymmetry = random.nextBoolean();
		else
			ySymmetry = true;
	}
	
	public void process() {
		generateShapeMatrix();
	}
	
	public void generateShapeMatrix() {
		houseShapeMatrix = new int[initialHouseShapeMatrixDim][initialHouseShapeMatrixDim];
		for (int i = 0; i < initialHouseShapeMatrixDim; i ++) {
			houseShapeMatrix[i] = new int[initialHouseShapeMatrixDim];
			Arrays.fill(houseShapeMatrix[i], 0);
		}
		
		houseShapeMatrix[initialHouseShapeMatrixDim / 2][initialHouseShapeMatrixDim / 2] = 1;

		Bound limit = new Bound(new Vector2(0, 0) , new Vector2(initialHouseShapeMatrixDim, initialHouseShapeMatrixDim));
		
		int noOfOccupiedBlocks = 0;
		
		while (noOfOccupiedBlocks < initialHouseShapeMatrixDim + random.nextInt(initialHouseShapeMatrixDim)) {
			int initX = random.nextInt(initialHouseShapeMatrixDim);
			int initY = random.nextInt(initialHouseShapeMatrixDim);
			while (houseShapeMatrix[initX][initY] != 0) {
				initX = random.nextInt(initialHouseShapeMatrixDim);
				initY = random.nextInt(initialHouseShapeMatrixDim);
			}
			Vector2 initPoint = new Vector2(initX, initY);
			System.out.println(initPoint);
			while (! isAdjacentToOccupiedShapeMatrixBlock(initPoint)) {
				int chosenCoordinate = random.nextInt(2);
				int chosenDirection = random.nextInt(3) - 1;

				if (chosenCoordinate == 0)
					initPoint.translateUptoLimit(chosenDirection, 0, limit);
				else
					initPoint.translateUptoLimit(0, chosenDirection, limit);
			}
			noOfOccupiedBlocks ++;
			manageAdjacencyDegree(initPoint);

			Vector2 tempPoint = new Vector2(initPoint);
				
			if (xSymmetry && initPoint.x != initialHouseShapeMatrixDim / 2) {
				tempPoint.x = initialHouseShapeMatrixDim - tempPoint.x - 1;
				manageAdjacencyDegree(tempPoint);
				noOfOccupiedBlocks ++;
			}
			tempPoint = new Vector2(initPoint);
			if (ySymmetry && initPoint.y != initialHouseShapeMatrixDim / 2) {
				tempPoint.y = initialHouseShapeMatrixDim - initPoint.y - 1;
				manageAdjacencyDegree(tempPoint);
				noOfOccupiedBlocks ++;
			}
			if (xSymmetry && initPoint.x != initialHouseShapeMatrixDim / 2 && ySymmetry && initPoint.y != initialHouseShapeMatrixDim / 2) {
				tempPoint.x = initialHouseShapeMatrixDim - tempPoint.x - 1;
				manageAdjacencyDegree(tempPoint);
				noOfOccupiedBlocks ++;
			}
		}
		
		for (int i = 0; i < initialHouseShapeMatrixDim; i ++) {
			for (int j = 0; j < initialHouseShapeMatrixDim; j ++) {
				if (isBlockConcavePositioned(i, j)) {
					manageAdjacencyDegree(new Vector2(i, j));
				}
			}
		}
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
	}
	
	private boolean isBlockConcavePositioned(int i, int j) {
		return (i > 0 && i < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[i - 1][j] == 1 && houseShapeMatrix[i + 1][j] == 1)
				|| (j > 0 && j < initialHouseShapeMatrixDim - 1 && houseShapeMatrix[i][j - 1] == 1 && houseShapeMatrix[i][j + 1] == 1);
	}
}
