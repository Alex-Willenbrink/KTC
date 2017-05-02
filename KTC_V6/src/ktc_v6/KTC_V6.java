/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v6;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
public class KTC_V6 extends Application {

    @Override
    public void start(Stage primaryStage) {

        Circle circle = new Circle();
        circle.setCenterX(500);
        circle.setCenterY(400);
        circle.setRadius(400);
        circle.setFill(Color.WHITE);
        Pane pane = new Pane();
        pane.getChildren().add(circle);
        pane.setStyle("-fx-background-color: black;");

        Cell cell = new Cell(pane, circle, new double[]{.5, .5, .5}, new double[]{0, 0, 0}); // Add first cell to circle
        CellMatrix cellMatrix = new CellMatrix(pane, circle);
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
        
        cellMatrix.bringCirclesFront();
        
        Button buttonGrow = new Button();
        buttonGrow.setLayoutX(20);
        buttonGrow.setLayoutY(20);
        buttonGrow.setMinSize(100, 20);
        buttonGrow.setText("Grow");
        buttonGrow.setOnAction(e -> {
            
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
            
            cellMatrix.bringCirclesFront();
        });
        
        Button buttonConnect = new Button();
        buttonConnect.setLayoutX(20);
        buttonConnect.setLayoutY(60);
        buttonConnect.setMinSize(100, 20);
        buttonConnect.setText("Connect");
        
        buttonConnect.setOnAction(e -> {
            cellMatrix.cellConnectionSearch();
            cellMatrix.bringCirclesFront();
        });
        
        
        
        Button buttonChemo = new Button();
        buttonChemo.setLayoutX(20);
        buttonChemo.setLayoutY(100);
        buttonChemo.setMinSize(100, 20);
        buttonChemo.setText("Chemotherapy");
        
        double[][] chemo = new double[3][2];
            chemo[0][0] = 0;
            chemo[0][1] = 1;
            chemo[1][0] = 0;
            chemo[1][1] = 1;
            chemo[2][0] = 0;
            chemo[2][1] = .9;
        
        buttonChemo.setOnAction(e -> {
            try {
                showChemoScreen();
                
            } catch (IOException ex) {
                Logger.getLogger(KTC_V6.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        pane.getChildren().addAll(buttonGrow, buttonConnect, buttonChemo);
        
        cell.getCircle().setStroke(Color.RED);

        Scene scene = new Scene(pane, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void showChemoScreen() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        
        Stage stage = new Stage();
        
        stage.setScene(new Scene( loader.load()));
        
        FXMLDocumentController controller = loader.<FXMLDocumentController>getController();
        controller.initialize();
        stage.show();   
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
