package edu.westga.cs3212.group5.nutritiontracker.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CreateFoodItemTypeSelectionPageController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button selectBaseFoodButton;
    @FXML
    private Button selectCompositFoodButton;

    @FXML
    void initialize() {
        assert selectBaseFoodButton != null : "fx:id=\"selectBaseFoodButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
        assert selectCompositFoodButton != null : "fx:id=\"selectCompositFoodButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";

    }

}
