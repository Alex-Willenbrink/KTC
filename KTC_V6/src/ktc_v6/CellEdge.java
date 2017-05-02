/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v6;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Alex
 */
public class CellEdge {
    
    private Line line;
    private Cell[] connection;
    
    public CellEdge(Cell cellStart, Cell cellEnd){
        Line line = new Line();
        
        // Set start and finish location
        line.setStartY(cellStart.getCircle().getCenterY());
        line.setStartX(cellStart.getCircle().getCenterX());
        line.setEndY(cellEnd.getCircle().getCenterY());
        line.setEndX(cellEnd.getCircle().getCenterX());
        
        double strokeWidth = 0;
        // Find StrokeWidth
        if(cellStart.getCircle().getRadius() < cellEnd.getCircle().getRadius()){
            strokeWidth = cellStart.getCircle().getRadius();
        } else {
            strokeWidth = cellEnd.getCircle().getRadius();
        }
        
        
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(strokeWidth / 5 + 1);
        this.line = line;
        this.connection = new Cell[]{cellStart, cellEnd};
    }
    
    public Line getLine(){
        return this.line;
    }

    public Cell[] getConnection() {
        return connection;
    }
}


