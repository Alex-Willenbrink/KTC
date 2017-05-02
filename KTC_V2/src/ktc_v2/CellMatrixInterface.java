/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v2;

import javafx.scene.shape.Circle;

/**
 *
 * @author Alex
 */
public interface CellMatrixInterface {
    void repliate();
    void infect();
    void turn();
    boolean checkCellCollisions(Cell newCell);
    boolean checkWithinBigCircle(Cell newCell);
}
