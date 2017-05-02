/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v6;

/**
 *
 * @author Alex
 */
public interface CellInterface {
    
    double[] generateColor(double[] colorOrigin);
    void die();
    CellEdge makeSafeConnection(Cell cellEnd);
    void destroyConnectionEnd(Cell cellEnd);
       
}
