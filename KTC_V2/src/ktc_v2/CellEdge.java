/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v2;

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
        line.setStartY(cellStart.getCircle().getCenterY());
        line.setStartX(cellStart.getCircle().getCenterX());
        line.setEndY(cellEnd.getCircle().getCenterY());
        line.setEndX(cellEnd.getCircle().getCenterX());
        line.setStrokeWidth(cellStart.getCircle().getRadius() / 4);
        line.setStroke(Color.WHITE);
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
