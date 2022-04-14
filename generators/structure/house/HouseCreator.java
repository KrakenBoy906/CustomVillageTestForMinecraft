package com.shourya.customvillage.generators.structure.house;
import com.shourya.customvillage.datatypes.Bound;
import com.shourya.customvillage.datatypes.Vector2;
import com.shourya.customvillage.util.UtilCompat;
import jdk.jshell.execution.Util;

import java.util.Arrays;
import java.util.Random;

public class HouseCreator
{
	Random random;
	HouseContext context;
	
	private static final int houseShapeMatrixDim = 5;
	public int[][] houseShapeMatrix;
	public HouseCreator(HouseContext context) {
		this.context = context;
		random = new Random();
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
		
		houseShapeMatrix[random.nextInt(houseShapeMatrixDim)][random.nextInt(houseShapeMatrixDim)] = 1;

		Bound limit = new Bound(new Vector2(0, 0) , new Vector2(houseShapeMatrixDim, houseShapeMatrixDim));
		
		for (int i = 0; i < houseShapeMatrixDim; i ++) {
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
			houseShapeMatrix[initPoint.x][initPoint.y] = 1;
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
}
