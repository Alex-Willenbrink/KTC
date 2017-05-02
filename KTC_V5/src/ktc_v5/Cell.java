/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v5;

import java.util.ArrayList;
import java.util.Random;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author Alex
 */
public class Cell implements CellInterface {

    private Circle circle;
    private double[] fillColor;
    private double[] strokeColor;
    private double[] cellLocation;
    private double cellDivideRatio = 0.99;
    private ArrayList<CellEdge> edges = new ArrayList<>();
    private CellMatrix cellMatrix = null;
    private Pane pane;

    // Don't need to generate any lines or cell connections for this
    public Cell(Pane pane, Circle circle, double[] fillColor, double[] strokeColor) {
        this.pane = pane;

        // Make the circle that represents that cell
        Circle circleNew = new Circle();
        circleNew.setRadius(circle.getRadius() / 32);
        circleNew.setStrokeWidth(circleNew.getRadius() / 3);
        circleNew.setStrokeType(StrokeType.INSIDE);
        this.circle = circleNew;

        circleNew.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                destroyConnections();
                destroyCell();
            }
        });

        // Find location to put circle
        double[] cellLocation = findValidCircleLocation(circle);
        circleNew.setCenterY(cellLocation[0]);
        circleNew.setCenterX(cellLocation[1]);

        this.cellLocation = new double[]{this.circle.getCenterY(), this.circle.getCenterX()};
        this.circle.setFill(new Color(fillColor[0], fillColor[1], fillColor[2], 1));
        this.circle.setStroke(new Color(strokeColor[0], strokeColor[1], strokeColor[2], 1));
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
    }

    public Cell(Pane pane, Cell cellOrigin) {
        this.pane = pane;
        Circle circleNew = new Circle();

        Circle circleOrigin = cellOrigin.getCircle();
        Random rand = new Random();

        // Fill Color properties
        double[] fillColor = generateColor(cellOrigin.getFillColor());
        circleNew.setFill(new Color(fillColor[0], fillColor[1], fillColor[2], 1));

        double radiusNew = generateRadius(cellOrigin, fillColor);
        circleNew.setRadius(radiusNew);

        double[] cellLocationNew = generateLocation(cellOrigin, radiusNew);

        circleNew.setCenterY(cellLocationNew[0]);
        circleNew.setCenterX(cellLocationNew[1]);

        // Stroke Color properties
        double[] strokeColor = cellOrigin.getStrokeColor();
        circleNew.setStroke(new Color(strokeColor[0], strokeColor[1], strokeColor[2], 1));

        // Other Circle Properties
        circleNew.setStrokeWidth(radiusNew / 3);
        circleNew.setStrokeType(StrokeType.INSIDE);

        // EventHandler for Circle
        circleNew.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                destroyConnections();
                destroyCell();
            }
        });

        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.circle = circleNew;
        this.cellLocation = new double[]{this.circle.getCenterY(), this.circle.getCenterX()};
    }

    @Override
    public double[] generateColor(double[] colorOrigin) {
        double[] colorFinal = new double[3];
        Random rand = new Random();

        for (int i = 0; i < 3; i++) {
            double low = colorOrigin[i];
            double high = 1 - colorOrigin[i];

            // Generate random number with gaussian distribution with mean: 0, std: .5
            double randGauss = rand.nextGaussian() / 2;

            if (high > low) {
                colorFinal[i] = colorOrigin[i] + randGauss * colorOrigin[i];
            } else {
                colorFinal[i] = colorOrigin[i] + randGauss * (1 - colorOrigin[i]);
            }

            // Safety for numbers more than 2 standard deviations and end up outside color range
            if (colorFinal[i] < 0) {
                colorFinal[i] = (colorFinal[i] % 1) + 1;
            } else if (colorFinal[i] > 1) {
                colorFinal[i] = (colorFinal[i] % 1);
            }
        }
        return colorFinal;
    }

    public double generateRadius(Cell cellOrigin, double[] colorNew) {
        // Based solely off and some cell matrix constrictions
        // Color of cell will also determine consistency of radius

        Random rand = new Random();

        double colorMultiplier = 0;

        // Make we have appropriate color items to deal with inputs
        if (colorNew.length == 3) {
            for (int i = 0; i < 3; i++) {
                if (rand.nextBoolean() && rand.nextBoolean()) {
                    colorMultiplier = colorMultiplier + colorNew[i] * Math.abs(rand.nextGaussian());
                } else {
                    colorMultiplier = colorMultiplier + colorNew[i];
                }
            }
        }

        // MIGHT NEED TO PUT A THROW ERROR IF NO CELL MATRIX ITEM IN CELL
        double matrixRadius = cellOrigin.getCellMatrix().getCircle().getRadius();
        double radiusEnd = matrixRadius / 64 + (matrixRadius / 64 * colorMultiplier);

        if (radiusEnd > matrixRadius / 16) {
            return matrixRadius / 16;
        } else {
            return radiusEnd;
        }
    }

    public double[] generateLocation(Cell cellOrigin, double radiusEnd) {
        /* 
        Generate distance from cell first (will be a function of distance of 
        Then generate the x and y vector
        similar to polar coordinates and will guarentee that new cell is located not inside new cell
         */
        Random rand = new Random();

        Circle circleOrigin = cellOrigin.getCircle();
        double separationMin = circleOrigin.getRadius() + radiusEnd;

        // 2 std will be separationMin + separationMin * 0.25 
        double separationFinal = separationMin + separationMin * Math.abs(rand.nextGaussian() / 4);

        // Generate Y and X vectors
        double y = rand.nextDouble() * 2 - 1;
        double x = rand.nextDouble() * 2 - 1;

        double angle = Math.atan2(y, x);

        double yChange = Math.sin(angle) * separationFinal;
        double xChange = Math.cos(angle) * separationFinal;

        double[] cellOriginLocation = cellOrigin.getCellLocation();
        return new double[]{cellOriginLocation[0] + yChange, cellOriginLocation[1] + xChange};
    }

    private double[] findValidCircleLocation(Circle circle) {
        double divRatio = this.circle.getRadius() / circle.getRadius();  // Establish circle radii ratio

        double bCircleRadius = circle.getRadius();
        double bCircleX = circle.getCenterX();
        double bCircleY = circle.getCenterY();

        double lowX = bCircleX - bCircleRadius + bCircleRadius * divRatio;
        double highX = bCircleX + bCircleRadius - bCircleRadius * divRatio;

        double lowY = bCircleY - bCircleRadius + bCircleRadius * divRatio;
        double highY = bCircleY + bCircleRadius - bCircleRadius * divRatio;

        Random rand = new Random();

        double randX = rand.nextDouble() * (highX - lowX) + lowX;
        double xChange = Math.abs(bCircleX - randX);
        double yChange = Math.pow(Math.pow(bCircleRadius - bCircleRadius * divRatio, 2) - Math.pow(xChange, 2), 0.5);
        double randY = 0;

        // Now we calculate the randY using limits provided by the randX value
        if (rand.nextBoolean()) {
            randY = rand.nextDouble() * yChange + bCircleY;
        } else {
            randY = rand.nextDouble() * yChange * -1 + bCircleY;
        }

        return new double[]{randY, randX};
    }

    @Override
    public void die() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void destroyCell() {
        // get rid of circle from pane
        // get rid of cell from cellMatrix
        this.pane.getChildren().remove(this.getCircle());
        if (this.cellMatrix != null) {
            this.cellMatrix.getCellArray().remove(this);
        }
    }

    @Override
    public CellEdge makeSafeConnection(Cell cellEnd) {

        // Compare all edge points to cellEnd
        for (CellEdge edge : this.edges) {
            Cell[] cellArray = edge.getConnection();
            if (cellArray[0] == cellEnd || cellArray[1] == cellEnd) {
                return null;
            }
        }

        CellEdge edge = new CellEdge(this, cellEnd);
        edges.add(edge);
        cellEnd.addCellEdge(edge);
        return edge;
    }
    
    public CellEdge tryCellConnection(Cell cellEnd){
        
        // Compare all edge points to cellEnd
        for (CellEdge edge : this.edges) {
            Cell[] cellArray = edge.getConnection();
            if (cellArray[0] == cellEnd || cellArray[1] == cellEnd) {
                return null;
            }
        }
        
        // if all colors are off by 0.1 (have a 99% of connecting)
        double colorFactor = 0;
        
        for(int i = 0; i < 3; i++){
            colorFactor = colorFactor + Math.pow(this.getFillColor()[i] - cellEnd.getFillColor()[i], 2);
        }
        
        colorFactor = 1 / Math.pow(colorFactor, 0.5);
        double connectFactor = Math.pow(1 / (1 + Math.pow(Math.E, -colorFactor)), 14);
        
        if(nextBooleanAboveX(connectFactor)){
            CellEdge edge = new CellEdge(this, cellEnd);
            edges.add(edge);
            cellEnd.addCellEdge(edge);
            return edge;
        } else {
            return null;
        }
    }
    
    public boolean nextBooleanAboveX(double x){
        Random rand = new Random();
        if(rand.nextFloat() < x){
            return true;
        } else return false;
    }

    public void destroyConnections() {
        // first go through all other cells attached to it and remove them in their lists
        // remove the item in the pane
        for (CellEdge edge : this.edges) {
            if (edge.getConnection()[0] == this) {
                destroyConnectionEnd(edge.getConnection()[1]);
            } else {
                destroyConnectionEnd(edge.getConnection()[0]);
            }
        }
        this.edges = null;
    }

    @Override
    public void destroyConnectionEnd(Cell cellEnd) {
        // Make sure connection exists
        CellEdge edgeCurr = null;

        for (CellEdge edge : this.edges) {
            Cell[] cellConnection = edge.getConnection();
            if (cellEnd == edge.getConnection()[0] || cellEnd == edge.getConnection()[1]) {
                edgeCurr = edge;
                break;
            }
        }
        if (edgeCurr == null) {
            return;
        }

        // Destroy pane object
        // Destroy object in opposing cell
        // Destroy object in this cell
        this.pane.getChildren().remove(edgeCurr.getLine());
        cellEnd.getEdges().remove(edgeCurr);
    }

    public void assignCellMatrix(CellMatrix cellMatrix) {
        this.cellMatrix = cellMatrix;
    }

    public void addCellEdge(CellEdge edge) {
        edges.add(edge);
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public double[] getFillColor() {
        return fillColor;
    }

    public void setFillColor(double[] fillColor) {
        this.fillColor = fillColor;
    }

    public double[] getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(double[] strokeColor) {
        this.strokeColor = strokeColor;
    }

    public ArrayList<CellEdge> getEdges() {
        return edges;
    }

    public CellMatrix getCellMatrix() {
        return cellMatrix;
    }

    public double[] getCellLocation() {
        return cellLocation;
    }

    public void setCellDivideRatio(double ratioNew) {
        this.cellDivideRatio = ratioNew;
    }

    public void increaseCellDivideRatio() {
        this.cellDivideRatio = Math.pow(this.cellDivideRatio, 0.5);
    }

    public void decreaseCellDivideRatio() {
        if (this.cellDivideRatio < 0.2) {
            this.cellDivideRatio = 0.2;
        } else {
            this.cellDivideRatio = Math.pow(this.cellDivideRatio, 2);
        }
    }

    public double getCellDivideRatio() {
        return this.cellDivideRatio;
    }

}
