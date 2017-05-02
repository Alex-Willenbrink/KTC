/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v6;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.layout.Pane;
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
    private Pane backPane;
    
    public CellMatrix(Pane backPane, Circle circle){
        this.backPane = backPane;
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
    
    public void bringCirclesFront(){
        for(Cell cell : this.cellArray){
            cell.getCircle().toFront();
        }
    }
    
    public void cellConnectionSearch(){
        for(Cell cellStart : cellArray){
            for(Cell cellEnd : cellArray){
                // Make sure aren't identical cells
                if(cellStart == cellEnd) continue;
                
                // Find cells that are 2.5 distance of radius
                // Are we already connected to this cell?
                if(2.5 * cellStart.getCircle().getRadius() > findCenterDistanceDifference(cellStart, cellEnd)){
                    CellEdge newEdge = cellStart.tryCellConnection(cellEnd);
                    if(newEdge != null){
                        this.backPane.getChildren().add(newEdge.getLine());
                    }
                }
                
            }
        }
    }
    
    public double findCenterDistanceDifference(Cell cellStart, Cell cellEnd){
        double[] centerStart = cellStart.getCellLocation();
        double[] centerEnd = cellEnd.getCellLocation();
        
        double distanceSquared = 0;
        for(int i = 0; i < 2; i++){
            distanceSquared = distanceSquared + Math.pow(centerStart[i] - centerEnd[i], 2);
        }
        
        return Math.pow(distanceSquared, 0.5);
    }
    
    public void chemoTherapy(CellChemo cellChemo){ 
        ArrayList<Cell> destroyList = new ArrayList<>();
        
        for(Cell cell : this.cellArray){
            double[] cellColor = cell.getFillColor();
            boolean cellStatus = true;
            
            // Checks to see if cell color falls into 
            for(int i = 0; i < cellChemo.colorRange.length; i++){
                if(cellColor[i] < cellChemo.colorRange[i][0] || cellColor[i] > cellChemo.colorRange[i][1]){
                    cellStatus = false; break;
                }
            }
            
            if(!cellStatus) continue; // Check to see if we should analyze this cell
            double destroyOdds = 1;
            
            switch(cellChemo.strength){
                case WEAK:
                    destroyOdds = 0.3;
                    break;
                case AVERAGE:
                    destroyOdds = 0.5;
                    break;
                case STRONG:
                    destroyOdds = 0.9;
                    break;
            }
            
            if(nextBooleanAboveX(1 - destroyOdds)){
                destroyList.add(cell);
            }
        }  
        
        for(Cell cell : destroyList){
            cell.destroyConnections();
            cell.destroyCell();
        }
    }
}
