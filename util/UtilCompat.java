package com.shourya.customvillage.util;

import com.shourya.customvillage.datatypes.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

public class UtilCompat {
	
	public static int[][] createArr(int length, int width, int value) throws IllegalArgumentException{
		if (length == Integer.MAX_VALUE || length < 1 || width == Integer.MAX_VALUE || width < 1) {
			throw new IllegalArgumentException("unsupported parameters");
		}
		
		int[][] arr = new int[length][width];
		
		for (int i = 0; i < length; i ++) {
			arr[i] = new int[width];
			Arrays.fill(arr[i], 0);
		}
		
		return arr;
	}

    public static int[][] scaleArr(int[][] arr, double widthMultiplier, double heightMultiplier) {
        int[][] newArr = new int[(int)(arr.length * widthMultiplier)][];

        for (float i = 0; i < newArr.length; i += 1 / widthMultiplier) {
            newArr[(int)i] = new int[(int)(arr.length * heightMultiplier)];
            for (float j = 0; j < newArr.length; j += 1 / heightMultiplier) {
                newArr[(int) i][(int) j] = arr[(int)(i / widthMultiplier)][(int)(j / heightMultiplier)];
            }
        }

        return newArr;
    }

    public static int[][] scaleArr2(int[][] arr, int oldWidth, int oldHeight, double widthMultiplier, double heightMultiplier) {
        int newWidth = (int)(oldWidth * widthMultiplier);
        int newHeight = (int)(oldHeight * heightMultiplier);
        int[][] newArray = new int[newWidth][newHeight];
        int k = 1;
        for(int i = 0; i < oldWidth; i++)
            for(int j = 0; j < oldHeight; j++)
                arr[i][j] = k++;

        for(int i = 0; i < newWidth; i++)
            for(int j = 0; j < newHeight; j++)
                newArray[i][j] = arr[(int)(i/widthMultiplier)][(int)(j/heightMultiplier)];


        return newArray;
    }

    public static int[][] scaleArr3(int[][] arr, double rfactor, double cfactor)
    {
        // create an empty matrix:
        int rows = (int) (arr.length * rfactor); // rows in the new matrix
        int[][] newArr = new int[rows][]; // our new, stretched matrix

        // loop through the rows and columns of the *new* matrix:
        for(int r = 0; r < rows; r++)
        {
            int cols = (int) (arr[(int) (r / rfactor)].length * cfactor); // columns in the new matrix row
            newArr[r] = new int[cols];
            for(int c = 0; c < cols; c++)
            {
                // Divide the row and column indices by the
                // appropriate factors to find the correct value
                // in the original matrix.
                // Integer division just drops any remainder,
                // which is what we want.
                newArr[r][c] = arr[(int) (r/rfactor)][(int) (c/cfactor)];
            }
        }
        return newArr;
    }

    public static int[][] trimArrayWithValue(int[][] arr, int value) {
        int columnStartsAt = 0;
        int columnEndsAt = arr.length - 1;

        int rowStartsAt = 0;
        int rowEndsAt = arr[arr.length - 1].length - 1;

        for (int i = 0; i < arr.length; i ++) {
            boolean hasAnyImportantValue = false;
            for (int j = 0; j < arr[i].length; j ++) {
                if (arr[i][j] != value) {
                    hasAnyImportantValue = true;
                    break;
                }
            }
            if (hasAnyImportantValue) {
                columnStartsAt = i;
                break;
            }
        }

        for (int i = arr.length - 1; i >= 0; i --) {
            boolean hasAnyImportantValue = false;
            for (int j = 0; j < arr[i].length; j ++) {
                if (arr[i][j] != value) {
                    hasAnyImportantValue = true;
                    break;
                }
            }
            if (hasAnyImportantValue) {
                columnEndsAt = i;
                break;
            }
        }

        for (int i = 0; i < arr.length; i ++) {
            boolean hasAnyImportantValue = false;
            for (int j = columnStartsAt; j <= columnEndsAt; j ++) {
                if (arr[j][i] != value) {
                    hasAnyImportantValue = true;
                    break;
                }
            }
            if (hasAnyImportantValue) {
                rowStartsAt = i;
                break;
            }
        }

        for (int i = arr.length - 1; i >= 0; i --) {
            boolean hasAnyImportantValue = false;
            for (int j = columnStartsAt; j <= columnEndsAt; j ++) {
                if (arr[j][i] != value) {
                    hasAnyImportantValue = true;
                    break;
                }
            }
            if (hasAnyImportantValue) {
                rowEndsAt = i;
                break;
            }
        }

        int[][] result = new int[columnEndsAt - columnStartsAt + 1][rowEndsAt - rowStartsAt + 1];

        for (int i = columnStartsAt; i <= columnEndsAt; i ++) {
            result[i - columnStartsAt] = new int[rowEndsAt - rowStartsAt + 1];
            for (int j = rowStartsAt; j <= rowEndsAt; j ++) {
                result[i - columnStartsAt][j - rowStartsAt] = arr[i][j];
            }
        }

        return result;
    }
    
    public static int[][] copyArray(int[][] arr) {
        int[][] outputArr = new int[arr.length][];
        for (int i = 0; i < arr.length; i ++) {
            outputArr[i] = new int[arr[i].length];
            for (int j = 0; j < arr[i].length; j ++) {
                outputArr[i][j] = arr[i][j];
            }
            //System.arraycopy(arr, 0, outputArr, 0, arr[i].length);
        }
        
        return outputArr;
    }

    public static void arrayToFile(int[][] arr, String filePath) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++)//for each row
        {
            for (int j = 0; j < arr[i].length; j++)//for each column
            {
                builder.append(arr[i][j]);//append to the output string
            }
            builder.append("\n");//append new line at the end of the row
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(builder.toString());//save the string representation of the board
        writer.close();
    }

    public static int[][] fileToArray(String filePath, int width, int height) throws Exception {

        int[][] outpurArr;
        outpurArr = new int[width][height];

        StringBuilder resultStringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));

        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            outpurArr[i] = new int[height];
            String[] valueStrings = line.split(" ");
            int j = 0;
            for (String valueString : valueStrings) {
                outpurArr[i][j] = Integer.parseInt(valueString);
                j ++;
            }
            i ++;
        }
        return outpurArr;
    }

    public static void printArray(int[][] arr, PrintStream printStream) {
        for (int i = 0; i < arr.length; i ++) {
            for (int j = 0; j < arr[i].length; j ++) {
                printStream.print(arr[i][j] + " ");
            }
            printStream.println();
        }
    }

    public static Color getColorFromValue(int value)
    {
        int R = 1, G = 2, B = 3;
        R = ((value * R + 200) * value) % 255;
        G = ((value * value * G) * value) % 255;
        B = ((value + value * B) * value) % 255;
        int contrast = 150;
        Color c = new Color(Math.min(R + contrast, 255), Math.min(G + contrast, 255), Math.min(B + contrast, 255));
        return c;
    }
}

