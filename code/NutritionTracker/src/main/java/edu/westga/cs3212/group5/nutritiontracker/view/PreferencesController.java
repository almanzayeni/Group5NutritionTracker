package edu.westga.cs3212.group5.nutritiontracker.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PreferencesController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label backButtonLabel;

    @FXML
    private TextField calorieTextField;

    @FXML
    private TextField carbsTextField;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField fatTextField;

    @FXML
    private TextField otherGoalsTextField;

    @FXML
    private ComboBox<?> primaryGoalComboBox;

    @FXML
    private TextField proteinTextField;

    @FXML
    private TextField sodiumTextField;

    @FXML
    private TextField sugarTextField;

    @FXML
    private Label warningLabel;

    @FXML
    void handleCreateAccount(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert this.backButtonLabel != null : "fx:id=\"backButtonLabel\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.calorieTextField != null : "fx:id=\"calorieTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.carbsTextField != null : "fx:id=\"carbsTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.createAccountButton != null : "fx:id=\"createAccountButton\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.fatTextField != null : "fx:id=\"fatTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.otherGoalsTextField != null : "fx:id=\"otherGoalsTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.primaryGoalComboBox != null : "fx:id=\"primaryGoalComboBox\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.proteinTextField != null : "fx:id=\"proteinTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.sodiumTextField != null : "fx:id=\"sodiumTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.sugarTextField != null : "fx:id=\"sugarTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.warningLabel != null : "fx:id=\"warningLabel\" was not injected: check your FXML file 'PreferencesPage.fxml'.";

    }

}
