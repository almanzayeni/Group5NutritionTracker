package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class CreateBaseFoodPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addFoodButton;

    @FXML
    private TextField caloriesTextField;

    @FXML
    private TextField carbohydratesTextField;

    @FXML
    private TextField fatTextField;

    @FXML
    private JFXHamburger hamburgerMenu;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Pane menuPane;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField portionSizeTextField;

    @FXML
    private Label portionSizeUnitLabel;

    @FXML
    private TextField proteinTextField;

    @FXML
    private ComboBox<?> quantityCategoryComboBox;

    @FXML
    private TextField sodiumTextField;

    @FXML
    private TextField sugarTextField;

    @FXML
    void initialize() {
        assert addFoodButton != null : "fx:id=\"addFoodButton\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert caloriesTextField != null : "fx:id=\"caloriesTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert carbohydratesTextField != null : "fx:id=\"carbohydratesTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert fatTextField != null : "fx:id=\"fatTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert hamburgerMenu != null : "fx:id=\"hamburgerMenu\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert menuPane != null : "fx:id=\"menuPane\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert nameTextField != null : "fx:id=\"nameTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert portionSizeTextField != null : "fx:id=\"portionSizeTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert portionSizeUnitLabel != null : "fx:id=\"portionSizeUnitLabel\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert proteinTextField != null : "fx:id=\"proteinTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert quantityCategoryComboBox != null : "fx:id=\"quantityCategoryComboBox\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert sodiumTextField != null : "fx:id=\"sodiumTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
        assert sugarTextField != null : "fx:id=\"sugarTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";

    }

}
