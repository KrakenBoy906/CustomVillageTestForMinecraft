package com.shourya.customvillage.generators.structure.house;

public class HouseContext
{
	public static final int STANDARD_SEED_LENGTH = 16;
	String seed;
	public int initialShapeMatrixDimSeed = 2;
	public int shapeOccupationSeed = 1;
	public int width;
	public int height;
	public boolean houseSymmetryX = true;
	public boolean houseSymmetryY = true;
	public int groundFloorHeight;
	public int otherThanGroundFloorHeight;
	public int noOfFloors;
	public int mainFloorIndex;
	public boolean hasMultipleEntryDoors;
	public boolean hasEntryDoorCover;
	public boolean hasMainFloorBalcony;
	public boolean hasOtherThanMainFloorBalcony;
	public boolean isHouseElevated;
	public boolean isBigHouse; // MEANS HAS JOB ROOM AS WELL AS REST ROOM AND SLEEPING ROOM
	
	public HouseContext(String seed) {
		this.seed = wellFormSeed(seed);
		width = Integer.parseInt(seed.substring(0, 3));
		height = Integer.parseInt(seed.substring(3, 6));
		
		System.out.println("width and height : " + width + " " + height);
	}
	
	public String wellFormSeed(String seed) {
		StringBuilder result = new StringBuilder(seed);
		for (int i = 0; i < result.length(); i ++) {
			if (result.charAt(i) < '0' || result.charAt(i) > '9') {
				result.deleteCharAt(i);
				i --;
			}
		}
		if (seed.length() < STANDARD_SEED_LENGTH) {
			while (result.length() < STANDARD_SEED_LENGTH) {
				result.append(0);
			}
		}
		System.out.println("modified seed : " + result);
		return result.toString();
	}
}
