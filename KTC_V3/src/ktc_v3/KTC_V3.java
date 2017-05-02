/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author Alex
 */
public class KTC_V3 extends Application {

    @Override
    public void start(Stage primaryStage) {

        Circle circle = new Circle();
        circle.setCenterX(500);
        circle.setCenterY(400);
        circle.setRadius(400);
        circle.setFill(Color.GRAY);
        Pane pane = new Pane();
        pane.getChildren().add(circle);

        Cell cell = new Cell(pane, circle, new double[]{.5, .5, .5}, new double[]{0, 0, 0}); // Add first cell to circle
        CellMatrix cellMatrix = new CellMatrix(circle);
        cellMatrix.addCell2Matrix(cell);
        cell.assignCellMatrix(cellMatrix);
        pane.getChildren().add(cell.getCircle());
        
        
        for (int i = 0; i < 20; i++) {
            cellMatrix.updateDivisionList();
            for (Cell cellCurrent : cellMatrix.getDivideList()) {
                Cell cellNew = new Cell(pane, cellCurrent);
                
                if(cellMatrix.checkWithinBigCircle(cellNew) && !cellMatrix.checkCellCollisions(cellNew)){
                    cellCurrent.increaseCellDivideRatio();
                    // Add Cell to cell Matrix
                    cellMatrix.addCell2Matrix(cellNew);
                    cellNew.assignCellMatrix(cellMatrix);
                    
                    // Make Cell edge
                    CellEdge edge = new CellEdge(cellCurrent, cellNew);
                    cellCurrent.addCellEdge(edge); cellNew.addCellEdge(edge);
                    
                    // Add cell to cellMatrix
                    pane.getChildren().addAll(cellNew.getCircle(), edge.getLine());
                } else{
                    cellCurrent.decreaseCellDivideRatio();
                }
            }
        }
        
        Button button = new Button();
        button.setLayoutX(20);
        button.setLayoutY(20);
        button.setText("Grow");
        
        
        button.setOnAction(e -> {
            cellMatrix.updateDivisionList();
            for (Cell cellCurrent : cellMatrix.getDivideList()) {
                Cell cellNew = new Cell(pane, cellCurrent);
                
                if(cellMatrix.checkWithinBigCircle(cellNew) && !cellMatrix.checkCellCollisions(cellNew)){
                    cellCurrent.increaseCellDivideRatio();
                    // Add Cell to cell Matrix
                    cellMatrix.addCell2Matrix(cellNew);
                    cellNew.assignCellMatrix(cellMatrix);
                    
                    // Make Cell edge
                    CellEdge edge = new CellEdge(cellCurrent, cellNew);
                    cellCurrent.addCellEdge(edge); cellNew.addCellEdge(edge);
                    
                    // Add cell to cellMatrix
                    pane.getChildren().addAll(cellNew.getCircle(), edge.getLine());
                } else{
                    cellCurrent.decreaseCellDivideRatio();
                }
            }
        });
        
        
        
        pane.getChildren().add(button);
        
        cell.getCircle().setStroke(Color.RED);

        Scene scene = new Scene(pane, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
