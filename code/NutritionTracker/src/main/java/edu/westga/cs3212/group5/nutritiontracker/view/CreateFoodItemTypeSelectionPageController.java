package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class CreateFoodItemTypeSelectionPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXHamburger hamburgerMenu;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Pane menuPane;

    @FXML
    private Button selectBaseFoodButton;

    @FXML
    private Button selectCompositFoodButton;

    @FXML
    void initialize() {
        assert hamburgerMenu != null : "fx:id=\"hamburgerMenu\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
        assert homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
        assert menuPane != null : "fx:id=\"menuPane\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
        assert selectBaseFoodButton != null : "fx:id=\"selectBaseFoodButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
        assert selectCompositFoodButton != null : "fx:id=\"selectCompositFoodButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";

    }

}
