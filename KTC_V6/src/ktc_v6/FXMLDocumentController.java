/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ktc_v6;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class FXMLDocumentController implements Initializable {
    
    
    @FXML 
    private ColorPicker colorPicker;
    
    
    @FXML 
    private ChoiceBox cbAggression;
    
    @FXML 
    private ChoiceBox cbColorSpec;
    
    @FXML 
    private Button buttonContinue;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println(cbAggression.getValue().toString());
        Color color = colorPicker.getValue();
        System.out.println(colorPicker.getValue());
        
        double[] colorDouble = new double[]{color.getRed(), color.getGreen(), color.getBlue()};
        
        
        // Find strength type
        Strength strength;
        switch(cbAggression.getValue().toString()){
            case "WEAK":
                strength = Strength.WEAK;
                break;
            case "AVERAGE":
                strength = Strength.AVERAGE;
                break;
            case "STRONG":
                strength = Strength.STRONG;
                break;
            default:
                strength = Strength.STRONG;
        }
        
        
        // Find Color specificity
        ColorRange colorSpec;
        switch(cbColorSpec.getValue().toString()){
            case "SPECIFIC":
                colorSpec = ColorRange.SPECIFIC;
                break;
            case "SEMI-SPECIFIC":
                colorSpec = ColorRange.SEMISPECIFIC;
                break;
            case "NONSPECIFIC":
                colorSpec = ColorRange.NONSPECIFIC;
                break;
            default:
                colorSpec = ColorRange.NONSPECIFIC;
        }
        
        CellChemo cellChemo = new CellChemo(colorDouble, colorSpec, strength);
        Stage stage = (Stage) buttonContinue.getScene().getWindow();
        stage.close();
    }
    
    
    
    //@Override
    public void initialize() {
        cbAggression.setItems(FXCollections.observableArrayList("WEAK", "AVERAGE", "STRONG"));
        cbAggression.getSelectionModel().selectFirst();  
        
        cbColorSpec.setItems(FXCollections.observableArrayList("SPECIFIC", "SEMI-SPECIFIC", "NONSPECIFIC"));
        cbColorSpec.getSelectionModel().selectFirst(); 
    }    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
