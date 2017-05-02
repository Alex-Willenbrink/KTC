package ktc;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class Cell {
    private Circle circle;
    private int row, col;
    private int[] coordinates;
    private double[] fillColor;
    private double[] strokeColor;
    private boolean startState;
    private boolean infectedState;
    
    public Cell(int row, int col){
        this.row = row;
        this.col = col;
        this.coordinates = new int[]{row, col};
        this.fillColor = new double[]{0.5,0.5,0.5,1};
        this.strokeColor = new double[]{0,0,0,1};
        this.startState = false;
        this.infectedState = false;
        
        Circle circle = new Circle();
        circle.setStrokeWidth(6);
        circle.setCenterX(30* (col+1));
        circle.setCenterY(30* (row+1));
        circle.setRadius(10);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);
        this.circle = circle;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public double[] getFillColor() {
        return fillColor;
    }

    public void setFillColor(double[] color) {
        this.fillColor = color;
        this.circle.setFill(new Color(this.fillColor[0], this.fillColor[1], this.fillColor[2], 1)); 
    }

    public double[] getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(double[] strokeColor) {
        this.strokeColor = strokeColor;
        this.circle.setStroke(new Color(this.strokeColor[0], this.strokeColor[1], this.strokeColor[2], 1));
    }

    public boolean getStartState() {
        return startState;
    }

    public void setStartState(boolean startState) {
        this.startState = startState;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }
    
     public boolean getInfectedState() {
        return infectedState;
    }

    public void setInfectedState(boolean infectedState) {
        this.infectedState = infectedState;
    }
    
    public double[] circleOuterLocation(double rowLoc, double colLoc){
        double trueRadius = this.circle.getRadius() + this.circle.getStrokeWidth();
        double center[] = new double[]{this.circle.getCenterY(), this.circle.getCenterX()};
        return new double[]{trueRadius * rowLoc + center[0], trueRadius * colLoc + center[1]};
    }
}
