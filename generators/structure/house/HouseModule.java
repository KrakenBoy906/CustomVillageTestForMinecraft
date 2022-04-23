package com.shourya.customvillage.generators.structure.house;

import com.shourya.customvillage.datatypes.Vector2;
import com.shourya.customvillage.datatypes.Bound;

public class HouseModule {
    public Vector2 startPoint;
    public int length = 0;
    public int width = 0;
    Direction direction = Direction.UP;
    
    public HouseModule(Vector2 startPoint, int length, int width, Direction direction) {
        this.startPoint = startPoint;
        this.length = length;
        this.width = width;
        this.direction = direction;
    }
	
	public HouseModule(Vector2 startPoint, Direction direction) {
		this.startPoint = startPoint;
		length = 1;
		width = 1;
		this.direction = direction;
	}
	
	public void lengthExpand(int extremety1Value, int extremety2Value, int[][] refMatrix, int refMatrixLength, int refMatrixWidth) {
		boolean extremetyValueFound = false;
		
		if (direction == Direction.LEFT) {
			
			extremetyValueFound = false;
			while (startPoint.x - length >= 0 && ! extremetyValueFound) {
				for (int j = 0; j < width; j ++) {
					if (refMatrix[startPoint.x - length][startPoint.y + j] == extremety1Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound)
					length ++;
			}

			extremetyValueFound = false;
			while (startPoint.x + 1 < refMatrixLength && ! extremetyValueFound) {
				for (int j = 0; j < width; j ++) {
					if (refMatrix[startPoint.x + 1][startPoint.y + j] == extremety2Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound) {
					length ++;
					startPoint.translate(1, 0);
				}
			}
		}
		
		else if (direction == Direction.RIGHT) {

			extremetyValueFound = false;
			while (startPoint.x - 1 >= 0 && ! extremetyValueFound) {
				for (int j = 0; j < width; j ++) {
					if (refMatrix[startPoint.x - 1][startPoint.y + j] == extremety1Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound) {
					startPoint.translate(-1, 0);
					length ++;
				}
			}

			extremetyValueFound = false;
			while (startPoint.x + length < refMatrixLength && ! extremetyValueFound) {
				for (int j = 0; j < width; j ++) {
					if (refMatrix[startPoint.x + 1][startPoint.y + j] == extremety2Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound)
					length ++;
			}
		}
		
		else if (direction == Direction.UP) {

			extremetyValueFound = false;
			while (startPoint.y - length >= 0 && ! extremetyValueFound) {
				for (int j = 0; j < width; j ++) {
					if (refMatrix[startPoint.x + j][startPoint.y - length] == extremety1Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound)
					length ++;
			}

			extremetyValueFound = false;
			while (startPoint.y + 1 < refMatrixWidth && ! extremetyValueFound) {
				for (int j = 0; j < width; j ++) {
					if (refMatrix[startPoint.x + j][startPoint.y + 1] == extremety2Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound) {
					length ++;
					startPoint.translate(0, 1);
				}
			}
		}

		else if (direction == Direction.DOWN) {

			extremetyValueFound = false;
			while (startPoint.y - 1 >= 0 && ! extremetyValueFound) {
				for (int j = 0; j < width; j ++) {
					if (refMatrix[startPoint.x + j][startPoint.y - 1] == extremety1Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound) {
					startPoint.translate(0, -1);
					length ++;
				}
			}

			extremetyValueFound = false;
			while (startPoint.y + length < refMatrixWidth && ! extremetyValueFound) {
				for (int j = 0; j < width; j ++) {
					if (refMatrix[startPoint.x + j][startPoint.y + 1] == extremety2Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound)
					length ++;
			}
		}
	}
	
	public void widthExpand(int extremety1Value, int extremety2Value, int[][] refMatrix, int refMatrixLength, int refMatrixWidth) {
		boolean extremetyValueFound = false;

		if (direction == Direction.LEFT) {

			extremetyValueFound = false;
			while (startPoint.y - 1 >= 0 && ! extremetyValueFound) {
				for (int j = 0; j < length; j ++) {
					if (refMatrix[startPoint.x - j][startPoint.y - 1] == extremety1Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound) {
					startPoint.translate(0, -1);
					width ++;
				}
			}

			extremetyValueFound = false;
			while (startPoint.y + width < refMatrixWidth && ! extremetyValueFound) {
				for (int j = 0; j < length; j ++) {
					if (refMatrix[startPoint.x - j][startPoint.y + width] == extremety2Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound)
					width ++;
			}
		}

		else if (direction == Direction.RIGHT) {

			extremetyValueFound = false;
			while (startPoint.y - 1 >= 0 && ! extremetyValueFound) {
				for (int j = 0; j < length; j ++) {
					if (refMatrix[startPoint.x + j][startPoint.y - 1] == extremety1Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound) {
					startPoint.translate(0, -1);
					width ++;
				}
			}

			extremetyValueFound = false;
			while (startPoint.y + width < refMatrixWidth && ! extremetyValueFound) {
				for (int j = 0; j < length; j ++) {
					if (refMatrix[startPoint.x + j][startPoint.y + width] == extremety2Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound) {
					width ++;
				}
			}
		}

		else if (direction == Direction.UP) {

			extremetyValueFound = false;
			while (startPoint.x - width >= 0 && ! extremetyValueFound) {
				for (int j = 0; j < length; j ++) {
					if (refMatrix[startPoint.x - width][startPoint.y - j] == extremety1Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound)
					width ++;
			}

			extremetyValueFound = false;
			while (startPoint.x + 1 < refMatrixWidth && ! extremetyValueFound) {
				for (int j = 0; j < length; j ++) {
					if (refMatrix[startPoint.x + 1][startPoint.y - j] == extremety2Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound) {
					width ++;
					startPoint.translate(1, 0);
				}
			}
		}

		else if (direction == Direction.DOWN) {

			extremetyValueFound = false;
			while (startPoint.x - width >= 0 && ! extremetyValueFound) {
				for (int j = 0; j < length; j ++) {
					if (refMatrix[startPoint.x - width][startPoint.y + j] == extremety1Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound)
					width ++;
			}

			extremetyValueFound = false;
			while (startPoint.x + 1 < refMatrixLength && ! extremetyValueFound) {
				for (int j = 0; j < width; j ++) {
					if (refMatrix[startPoint.x - width][startPoint.y + j] == extremety2Value) {
						extremetyValueFound = true;
						break;
					}
				}
				if (!extremetyValueFound)
					width ++;
			}
		}
	}
	
	public void boxExpand(int extremetyValue, int[][] refMatrix, int refMatrixLength, int refMatrixWidth, boolean lengthFirst) {
		if (lengthFirst) {
			lengthExpand(extremetyValue, extremetyValue, refMatrix, refMatrixLength, refMatrixWidth);
			widthExpand(extremetyValue, extremetyValue, refMatrix, refMatrixLength, refMatrixWidth);
		}
		else {
			widthExpand(extremetyValue, extremetyValue, refMatrix, refMatrixLength, refMatrixWidth);
			lengthExpand(extremetyValue, extremetyValue, refMatrix, refMatrixLength, refMatrixWidth);
		}
	}
	
	public boolean intersects(HouseModule other) {
		Bound b1 = getBound();
		Bound b2 = other.getBound();
		
		return b1.intersects(b2);
	}
	
	public Bound getBound() {
		Bound bound;
		
		switch(direction) {
			case UP : {
				bound = new Bound(startPoint, new Vector2(startPoint.x + width, startPoint.y - length));
				break;
			}
			case DOWN : {
				bound = new Bound(startPoint, new Vector2(startPoint.x + width, startPoint.y + length));
				break;
			}
			case LEFT : {
				bound = new Bound(startPoint, new Vector2(startPoint.x + width, startPoint.y - length));
				break;
			}
			default : {
				bound = new Bound(startPoint, new Vector2(startPoint.x + width, startPoint.y + length));
				break;
			}
		}
		return bound;
	}
	
	public HouseModule mirror(boolean xSymmetry, boolean ySymmetry, int[][] refMatrix, int refMatrixLength, int refMatrixWidth) {
		
		HouseModule houseModule = this.copy();
		
		if (xSymmetry) {
			houseModule.startPoint.x = refMatrixLength - houseModule.startPoint.x - 1;
			if (houseModule.direction == Direction.UP || houseModule.direction == Direction.DOWN) {
				if (houseModule.startPoint.x < refMatrixLength / 2)
					houseModule.startPoint.translate(-houseModule.width + 1, 0);
				else if (houseModule.startPoint.x > refMatrixLength / 2)
					houseModule.startPoint.translate(houseModule.width - 1, 0);
			}
			else {
				houseModule.direction = getReverseDirection(houseModule.direction);
			}
		}
		
		if (ySymmetry) {
			houseModule.startPoint.y = refMatrixWidth - houseModule.startPoint.y - 1;
			if (houseModule.direction == Direction.LEFT || houseModule.direction == Direction.RIGHT) {
				if (houseModule.startPoint.y < refMatrixWidth / 2)
					houseModule.startPoint.translate(0, -houseModule.width + 1);
				else if (houseModule.startPoint.y > refMatrixWidth / 2)
					houseModule.startPoint.translate(0, houseModule.width - 1);
			}
			else {
				houseModule.direction = getReverseDirection(houseModule.direction);
			}
		}
		
		return houseModule;
	}
	
	public HouseModule copy() {
		return new HouseModule(startPoint, length, width, direction);
	}

	public boolean equals(HouseModule other)
	{
		return startPoint.equals(other.startPoint) && direction == other.direction && length == other.length && width == other.width;
	}
    
    public boolean equalsWithOrExceptDirection(HouseModule other, int refMatrixLength, int refMatrixWidth) {
        if (this.equals(other))
            return true;
            
        switch (direction) {
            case LEFT : return other.direction == Direction.RIGHT && (other.startPoint.x == refMatrixLength - startPoint.x - 1) && other.startPoint.y == startPoint.y;
            case RIGHT : return other.direction == Direction.LEFT && (other.startPoint.x == refMatrixLength - startPoint.x - 1) && other.startPoint.y == startPoint.y;
            case UP : return other.direction == Direction.DOWN && (other.startPoint.y == refMatrixLength - startPoint.y - 1) && other.startPoint.x == startPoint.x;
            default : return other.direction == Direction.RIGHT && (other.startPoint.y == refMatrixLength - startPoint.y - 1) && other.startPoint.x == startPoint.x;
        }
    }

	@Override
	public String toString()
	{
		return "sp=[" + startPoint + "]; l=" + length + "; w=" + width + "; dir=" + direction;
	}
	
	private Direction getReverseDirection(Direction direction) {
		switch (direction) {
			case LEFT : return Direction.RIGHT;
			case RIGHT : return Direction.LEFT;
			case UP : return Direction.DOWN;
			case DOWN : return Direction.UP;
			default : return Direction.LEFT;
		}
	}

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }
}
