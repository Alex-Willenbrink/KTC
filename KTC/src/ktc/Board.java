/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

public class Board {

    private Cell[][] cellMatrix;
    private int row, col;
    private ArrayList<int[]> startList = new ArrayList<>();
    private ArrayList<int[]> infectedList = new ArrayList<>();
    

    public Board(int row, int col) {
        this.row = row;
        this.col = col;
        cellMatrix = new Cell[row][col];
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                
                // Initialize cell matrix
                cellMatrix[i][j] = new Cell(i, j);
            }
        }
        this.cellMatrix = cellMatrix;
    }

    public int getRow(){
        return this.row;
    }
    
    public int getCol(){
        return this.col;
    }

    public Cell[][] getCellMatrix() {
        return cellMatrix;
    }
    
    public void randomHitStart() {
        // Randomly will change a 0 to a 1 to indicate infection
        int row = new Random().nextInt(this.row);
        int col = new Random().nextInt(this.col);
        
        // Set the cell to starting
        this.cellMatrix[row][col].setStartState(true);
        this.cellMatrix[row][col].setFillColor(new double[]{.9, .9, .1});
        this.cellMatrix[row][col].setStrokeColor(new double[]{0, 0, 1});
        this.startList.add(new int[]{row, col});
    }
    
    public void randomHitInfection() {
        // Randomly will change a 0 to a 1 to indicate infection
        int row = new Random().nextInt(this.row);
        int col = new Random().nextInt(this.col);
        
        // Set the cell to infected
        this.cellMatrix[row][col].setInfectedState(true);
        this.cellMatrix[row][col].setStrokeColor(new double[]{1, 0, 0});
        this.infectedList.add(new int[]{row, col});
    }
    

    private boolean inBoard(int[] coordinates) {
        if (coordinates.length != 2) {
            return false;
        } else if (coordinates[0] < 0 || coordinates[0] > row - 1) {
            return false;
        } else if (coordinates[1] < 0 || coordinates[1] > col - 1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean boardStartState() {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                if (!this.cellMatrix[i][j].getStartState()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean boardInfectedState() {
        for (int i = 0; i < this.row; i++) {
            for (int j = 0; j < this.col; j++) {
                if (!this.cellMatrix[i][j].getInfectedState()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    public void spreadStart() {
        // Safety condition to make sure we're processing something
        if (this.startList.isEmpty()) {
            
            return;
        }
        
        // Now we go about infecting every applicable point and add new points to our list
        int listSize = this.startList.size();
        for (int m = 0; m < listSize; m++) {
            int[] matPoint = this.startList.remove(0);
            this.startList.add(matPoint);
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    int row = matPoint[0] + i;
                    int col = matPoint[1] + j;
                    int[] point = new int[]{row, col};
                    double[] colorOrigin = this.cellMatrix[matPoint[0]][matPoint[1]].getFillColor();
                    // Make sure that 1st, point is within board, and that 2nd point isn't infected
                    if (inBoard(point) && !this.cellMatrix[row][col].getStartState()) {
                        if(new Random().nextBoolean()) {
                            this.cellMatrix[row][col].setStartState(true); // start new point on matrix
                            double[] colorNew = generateColor(colorOrigin);
                            this.cellMatrix[row][col].setFillColor(colorNew);
                            this.startList.add(point);
                        }
                    }
                }
            }
        }
        // check for start status. keep repeating until start status is true
        if(!boardStartState()){
            spreadStart(); // Just keep repeating process until entire board is filled 
        }
    }

    
    public void spreadInfection() {
        // Safety condition to make sure we're processing something
        if (this.infectedList.isEmpty() || this.boardInfectedState()) {
            return;
        }
        
        // Now we go about infecting every applicable point and add new points to our list
        int listSize = this.infectedList.size();
        for (int m = 0; m < listSize; m++) {
            int[] matPoint = this.infectedList.remove(0);
            this.infectedList.add(matPoint); 
        
        /*  
            if you want, you can check at the end of algorithm, see if there
            are any loose ends. If they exist, pop the cell back onto the list, else
            don't pop it back onto the list. Will only be essential when the program
            gets big though.
            
        */

            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    int row = matPoint[0] + i;
                    int col = matPoint[1] + j;
                    int[] point = new int[]{row, col};
                    double[] colorOrigin = this.cellMatrix[matPoint[0]][matPoint[1]].getFillColor();
                    // Make sure that 1st, point is within board, and that 2nd point isn't infected
                    if (inBoard(point) && !this.cellMatrix[row][col].getInfectedState()) {
                        double[] colorFinal = this.cellMatrix[row][col].getFillColor();
                        if(jump(colorOrigin, colorFinal)) {
                            this.cellMatrix[row][col].setInfectedState(true); // start new point on matrix
                            this.cellMatrix[row][col].setStrokeColor(new double[]{1, 0, 0});
                            this.infectedList.add(point);
                        }
                    }
                }
            }
        }
    }
    
    public double[] generateColor(double[] colorOrigin){
        double[] colorFinal = new double[3];
        Random rand = new Random();
        
        for(int i = 0; i < 3; i++){
            double low = colorOrigin[i];
            double high = 1 - colorOrigin[i];
            
            // Generate random number with gaussian distribution with mean: 0, std: .5
            double randGauss = rand.nextGaussian() / 2;
            
            if(high > low) {
                colorFinal[i] = colorOrigin[i] + randGauss * colorOrigin[i];   
            } else{
                colorFinal[i] = colorOrigin[i] + randGauss * (1 - colorOrigin[i]);
            }
            
            // Safety for numbers more than 2 standard deviations and end up outside color range
            if(colorFinal[i] < 0){
                colorFinal[i] = (colorFinal[i] % 1) + 1; 
            } else if(colorFinal[i] > 1){
                colorFinal[i] = (colorFinal[i] % 1); 
            }
        }
        return colorFinal;
    }
    
    public boolean jump(double[] colorOrigin, double[] colorFinal){
        
        // Safety condition to make sure we're dealing with valid inputs
        if(colorOrigin.length != 3 || colorFinal.length != 3) return false;
        
        // Making an algorithm to simulate the virus jumping from 1 cell to another
        double num = 0;
        for(int i = 0; i < 3; i++){
            num = num + Math.pow(colorOrigin[i] - colorFinal[i], 2.0);
        }
        num = 1/Math.pow(num, 0.5);
        Random rand = new Random();
        
        if(num + rand.nextGaussian() > 3){
            return true;
        } else{
            return false;
        }
    }
}
