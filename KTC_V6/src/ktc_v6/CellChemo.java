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
public class CellChemo {

    double[][] colorRange;
    double[] color;
    Strength strength;
    ColorRange specificity;

    public CellChemo(double[] color, ColorRange specificity, Strength strength) {
        this.color = color;
        this.strength = strength;
        this.specificity = specificity;
        double[][] colorRange = new double[3][2];
        double colorDifference = 0.5;

        switch (this.specificity) {
            case SPECIFIC:
                colorDifference = 0.1;
                break;
            case SEMISPECIFIC:
                colorDifference = 0.2;
                break;
            case NONSPECIFIC:
                colorDifference = 0.3;
                break;
        }

        for (int i = 0; i < 3; i++) {
            colorRange[i][0] = color[i] - colorDifference;
            colorRange[i][1] = color[i] + colorDifference;

            // Deal with negative input
            if (colorRange[i][0] < 0) {
                colorRange[i][0] = 0;
            }

            // Deal with above 1 inputs
            if (colorRange[i][1] > 1) {
                colorRange[i][1] = 1;
            }
        }
        this.colorRange = colorRange;
    }
}
