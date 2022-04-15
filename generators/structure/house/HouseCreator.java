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
	
	private static final int houseShapeMatrixDim = 5;
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
		houseShapeMatrix = new int[houseShapeMatrixDim][houseShapeMatrixDim];
		for (int i = 0; i < houseShapeMatrixDim; i ++) {
			houseShapeMatrix[i] = new int[houseShapeMatrixDim];
			Arrays.fill(houseShapeMatrix[i], 0);
		}
		
		houseShapeMatrix[houseShapeMatrixDim / 2][houseShapeMatrixDim / 2] = 1;

		Bound limit = new Bound(new Vector2(0, 0) , new Vector2(houseShapeMatrixDim, houseShapeMatrixDim));
		
		int noOfOccupiedBlocks = 0;
		
		while (noOfOccupiedBlocks < houseShapeMatrixDim + random.nextInt(houseShapeMatrixDim)) {
			int initX = random.nextInt(houseShapeMatrixDim);
			int initY = random.nextInt(houseShapeMatrixDim);
			while (houseShapeMatrix[initX][initY] != 0) {
				initX = random.nextInt(houseShapeMatrixDim);
				initY = random.nextInt(houseShapeMatrixDim);
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
				
			if (xSymmetry) {
				houseShapeMatrix[houseShapeMatrixDim - initPoint.x - 1][initPoint.y] = 1;
				noOfOccupiedBlocks ++;
			}
			if (ySymmetry) {
				houseShapeMatrix[initPoint.x][houseShapeMatrixDim - initPoint.y - 1] = 1;
				noOfOccupiedBlocks ++;
			}
			if (xSymmetry && ySymmetry) {
				houseShapeMatrix[houseShapeMatrixDim - initPoint.x - 1][houseShapeMatrixDim - initPoint.y - 1] = 1;
				noOfOccupiedBlocks ++;
			}
		}
		
		for (int i = 0; i < houseShapeMatrixDim; i ++) {
			for (int j = 0; j < houseShapeMatrixDim; j ++) {
				if (isBlockConcavePositioned(i, j)) {
					houseShapeMatrix[i][j] = 1;
				}
			}
		}
		System.out.println("final matrix : ");
		UtilCompat.printArray(houseShapeMatrix, System.out);
	}

	private boolean isAdjacentToOccupiedShapeMatrixBlock(Vector2 pos) {
		int adjacency = 0;
		if ((pos.x > 0 && houseShapeMatrix[pos.x - 1][pos.y] > 0) || (pos.x < houseShapeMatrixDim - 1 && houseShapeMatrix[pos.x + 1][pos.y] > 0)) {
			adjacency ++;
		}
		if ((pos.y > 0 && houseShapeMatrix[pos.x][pos.y - 1] > 0) || (pos.y < houseShapeMatrixDim - 1 && houseShapeMatrix[pos.x][pos.y + 1] > 0)) {
			adjacency ++;
		}
		return adjacency == 1;
	}
	
	private void manageAdjacencyDegree(Vector2 pos) {

		if (pos.x > 0 && houseShapeMatrix[pos.x - 1][pos.y] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x - 1][pos.y] ++;
		}
		if (pos.x < houseShapeMatrixDim - 1 && houseShapeMatrix[pos.x + 1][pos.y] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x + 1][pos.y] ++;
		}

		if (pos.y > 0 && houseShapeMatrix[pos.x][pos.y - 1] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x][pos.y - 1] ++;
		}
		if (pos.y < houseShapeMatrixDim - 1 && houseShapeMatrix[pos.x][pos.y + 1] > 0) {
			houseShapeMatrix[pos.x][pos.y] ++;
			houseShapeMatrix[pos.x][pos.y + 1] ++;
		}
	}
	
	private boolean isBlockConcavePositioned(int i, int j) {
		return (i > 0 && i < houseShapeMatrixDim - 1 && houseShapeMatrix[i - 1][j] == 1 && houseShapeMatrix[i + 1][j] == 1)
				|| (j > 0 && j < houseShapeMatrixDim - 1 && houseShapeMatrix[i][j - 1] == 1 && houseShapeMatrix[i][j + 1] == 1);
	}
}
