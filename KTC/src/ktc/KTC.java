/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;



public class KTC extends Application{

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage){
        
        // Start by making board to play the game
        Board board = new Board(10, 50);
        
        Pane pane = new Pane();
        
        
        for(int i = 0; i < board.getRow(); i++){
            for(int j = 0; j < board.getCol(); j++){
                pane.getChildren().add(board.getCellMatrix()[i][j].getCircle());
            }
        }
        
        
        // Test to make lines that go down
        for(int i = 0; i < board.getRow() - 1; i++){
            for (int j = 0; j < board.getCol(); j++){
                Line line = new Line();
                double[] locationStart = board.getCellMatrix()[i][j].circleOuterLocation(1.0f, 0);
                double[] locationEnd = board.getCellMatrix()[i+1][j].circleOuterLocation(-1, 0);
                
                line.setStartY(locationStart[0]);
                line.setStartX(locationStart[1]);
                
                line.setEndY(locationEnd[0]);
                line.setEndX(locationEnd[1]);
                
                line.setStrokeWidth(4);
                pane.getChildren().add(line);
            }
        }
        
        
        // Test to make lines that go right
        for(int i = 0; i < board.getRow(); i++){
            for (int j = 0; j < board.getCol() - 1; j++){
                Line line = new Line();
                double[] locationStart = board.getCellMatrix()[i][j].circleOuterLocation(0, 1);
                double[] locationEnd = board.getCellMatrix()[i][j+1].circleOuterLocation(0, -1);
                
                line.setStartY(locationStart[0]);
                line.setStartX(locationStart[1]);
                
                line.setEndY(locationEnd[0]);
                line.setEndX(locationEnd[1]);
                
                line.setStrokeWidth(4);
                pane.getChildren().add(line);
            }
        }
        
        // Let's make diaganol lines going right
        for(int i = 0; i < board.getRow() - 1; i++){
            for (int j = 0; j < board.getCol() - 1; j++){
                Line line = new Line();
                double[] locationStart = board.getCellMatrix()[i][j].circleOuterLocation(Math.pow(2, 0.5), Math.pow(2, 0.5));
                double[] locationEnd = board.getCellMatrix()[i+1][j+1].circleOuterLocation(-Math.pow(2, 0.5), -Math.pow(2, 0.5));
                
                line.setStartY(locationStart[0]);
                line.setStartX(locationStart[1]);
                
                line.setEndY(locationEnd[0]);
                line.setEndX(locationEnd[1]);
                
                line.setStrokeWidth(4);
                pane.getChildren().add(line);
            }
        }
        
        
        // Let's make diaganol lines going left
        for(int i = 0; i < board.getRow() - 1; i++){
            for (int j = 1; j < board.getCol(); j++){
                Line line = new Line();
                double[] locationStart = board.getCellMatrix()[i][j].circleOuterLocation(Math.pow(2, 0.5), -Math.pow(2, 0.5));
                double[] locationEnd = board.getCellMatrix()[i+1][j-1].circleOuterLocation(-Math.pow(2, 0.5), Math.pow(2, 0.5));
                
                line.setStartY(locationStart[0]);
                line.setStartX(locationStart[1]);
                
                line.setEndY(locationEnd[0]);
                line.setEndX(locationEnd[1]);
                
                line.setStrokeWidth(4);
                pane.getChildren().add(line);
            }
        }
        
        
        // Randomize the starting state of the board board
        board.randomHitStart();
        board.spreadStart();
        
        
        // Next, we make the scene
        Scene scene = new Scene(pane, (board.getCol()+1)*30, (board.getRow() + 1)*30);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        board.randomHitInfection();
        // Let's make a button on a separate stage
        Button button = new Button("SPREAD");
        
        // Make lambda expression for button to spread the infected circles
        
        button.setOnAction(e -> {
            board.spreadInfection();
        });
        
        button.setLayoutX(25);
        button.setLayoutY(25);
        
        Pane paneButton = new Pane();
        paneButton.getChildren().add(button);
        Scene sceneButton = new Scene(paneButton, 100, 100);
        Stage stageButton = new Stage();
        stageButton.setScene(sceneButton);
        stageButton.show();
    }

}
