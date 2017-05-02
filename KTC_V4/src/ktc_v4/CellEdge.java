/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v4;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Alex
 */
public class CellEdge {
    
    private Line line;
    private Cell[] connection;
    private EdgeStrength edgeStrength;
    
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
        
        // Calculate EdgeStrength
        EdgeStrength strength = calculateEdgeStrength(cellStart, cellEnd);
        
        switch (strength) {
            case STRONG:
                line.setStroke(Color.GREEN);
                line.setStrokeWidth(strokeWidth / 3 + 1);
                break;
            case AVERAGE:
                line.setStroke(Color.YELLOW);
                line.setStrokeWidth(strokeWidth / 4 + 1);
                break;
            case WEAK:
                line.setStroke(Color.RED);
                line.setStrokeWidth(strokeWidth / 5 + 1);
                break;
            default:
                line.setStroke(Color.WHITE);
                line.setStrokeWidth(strokeWidth / 2 + 1);
        }
        
        this.edgeStrength = strength;
        this.line = line;
        this.connection = new Cell[]{cellStart, cellEnd};
    }
    
    public Line getLine(){
        return this.line;
    }

    public Cell[] getConnection() {
        return connection;
    }
    
    public EdgeStrength calculateEdgeStrength(Cell cellStart, Cell cellEnd){
        // base off solely color for now
        // can add distance and radius differences later
        double colorFactor = 0;
        
        for(int i = 0; i < 3; i++){
            colorFactor = colorFactor + Math.pow(cellStart.getFillColor()[i] - cellEnd.getFillColor()[i], 2);
        }
        
        colorFactor = 1 / Math.pow(colorFactor, 0.5); 
        
        /* 
        GREEN - above 5
        YELLOW - above 2
        RED - below or equal to 2
        */
        
        if(colorFactor > 5){
            return EdgeStrength.STRONG;
        } else if(colorFactor > 2){
            return EdgeStrength.AVERAGE;
        } else {
            return EdgeStrength.WEAK;
        }
    }
}

enum EdgeStrength {
    WEAK, AVERAGE, STRONG;
}
