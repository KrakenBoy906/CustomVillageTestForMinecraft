package com.shourya.customvillage.generators.structure.house;
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
		
	}
	
	public void generateShapeMatrix() {
		houseShapeMatrix = new int[houseShapeMatrixDim][houseShapeMatrixDim];
		for (int i = 0; i < houseShapeMatrixDim; i ++) {
			houseShapeMatrix[i] = new int[houseShapeMatrixDim];
			Arrays.fill(houseShapeMatrix[i], 0);
		}
		
		houseShapeMatrix[random.nextInt(houseShapeMatrixDim)][random.nextInt(houseShapeMatrixDim)] = 1;
		
		for (int i = 0; i < 6; i ++) {
			int currX = random.nextInt(houseShapeMatrixDim);
			int currY = random.nextInt(houseShapeMatrixDim);
		}
	}
}
