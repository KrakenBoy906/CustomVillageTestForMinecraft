package com.shourya.customvillage.util;

import java.awt.*;
import java.io.*;

public class Utils {
    public static void drawArr(int[][] arr, int width, int height, Graphics g) {
        for (int i = 0; i < width; i ++) {
            for (int j = 0; j < height; j ++) {
                //System.out.println("drawing : " + i + " " + j + " : " + arr[i][j]);
                g.setColor(new Color((arr[i][j] + 64) * 255 / 376, (arr[i][j] + 64) * 255 / 376, (arr[i][j] + 64) * 255 / 376));
                g.drawRect(i, j, 1, 1);
            }
        }
    }
    public static void drawArr(int[][] arr, Graphics g, DrawingType T) {
        for (int i = 0; i < arr.length; i ++) {
            for (int j = 0; j < arr[i].length; j ++) {
                if (T == DrawingType.GRAYSCALE)
                    g.setColor(new Color((arr[i][j] + 64) * 255 / 376, (arr[i][j] + 64) * 255 / 376, (arr[i][j] + 64) * 255 / 376));
                else if (T == DrawingType.RANDOMCOLOR)
                    g.setColor(getColorFromValue(arr[i][j]));
                else if (T == DrawingType.ONLY_1_AS_BLACK) {
                    if (arr[i][j] != 1) continue;
                    else g.setColor(Color.BLACK);
                }
                g.drawRect(i, j, 1, 1);
            }
        }
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

    public static enum DrawingType {
        GRAYSCALE,
        RANDOMCOLOR,
        ONLY_1_AS_BLACK
    }
}