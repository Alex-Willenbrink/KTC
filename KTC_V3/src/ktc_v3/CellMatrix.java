/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v3;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Alex
 */
public class CellMatrix implements CellMatrixInterface{
    
    private ArrayList<Cell> cellArray = new ArrayList<>();
    private ArrayList<Cell> divideList = new ArrayList<>();
    private Circle circle;
    
    public CellMatrix(Circle circle){
        this.circle = circle;
    }

    @Override
    public void repliate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void infect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void turn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean checkCellCollisions(Cell newCell) {
        for (Cell currCell : cellArray) {
            Shape intersect = Shape.intersect(currCell.getCircle(), newCell.getCircle());
            if (intersect.getBoundsInLocal().getWidth() != -1) {
                return true;
            }
        }
        return false;
    }
    
    public void addCell2Matrix(Cell newCell){
        this.cellArray.add(newCell);
    }

    public ArrayList<Cell> getCellArray() {
        return this.cellArray;
    }

    public Circle getCircle() {
        return this.circle;
    }

    // You'll have to make this more dynamic later
    @Override
    public boolean checkWithinBigCircle(Cell cellNew) {  
        double xDiff = this.circle.getCenterX() - cellNew.getCircle().getCenterX();
        double yDiff = this.circle.getCenterY() - cellNew.getCircle().getCenterY();
        double rCondition = this.circle.getRadius() - cellNew.getCircle().getRadius();

        return Math.pow(Math.pow(yDiff, 2) + Math.pow(xDiff, 2), 0.5) <= rCondition;
    }
    
    public void updateDivisionList(){
        divideList = new ArrayList<>();
        for(Cell cell : cellArray){
            if(checkForNextDivision(cell)){
                divideList.add(cell);
            }
        }
    }
    
    public boolean checkForNextDivision(Cell cell){
        if(nextBooleanAboveX(cell.getCellDivideRatio())){
             return true;
        }
        return false;
    }
    
    public boolean nextBooleanAboveX(double x){
        Random rand = new Random();
        if(rand.nextFloat() < x){
            return true;
        } else return false;
    }

    public ArrayList<Cell> getDivideList() {
        return divideList;
    }
    
    
}
