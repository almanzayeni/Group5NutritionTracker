package edu.westga.cs3212.group5.nutritiontracker.view;

import java.net.URL;
import java.util.ResourceBundle;

import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.UserPreferences;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.PreferencesVM;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

/**
 * Controller for the Preferences page.
 *
 * @author Yeni Almanza
 * @version spring 2026
 */
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
    private ComboBox<PrimaryGoal> primaryGoalComboBox;

    @FXML
    private TextField proteinTextField;

    @FXML
    private TextField sodiumTextField;

    @FXML
    private TextField sugarTextField;

    @FXML
    private Label warningLabel;

    private PreferencesVM viewModel;

    @FXML
    void handleCreateAccount(ActionEvent event) {
        PrimaryGoal selectedGoal = this.primaryGoalComboBox.getValue();
        if (selectedGoal == null) {
            this.warningLabel.setText("Please select a primary goal.");
            return;
        }

        String otherGoals = this.otherGoalsTextField.getText();
        UserPreferences prefs = this.viewModel.createPreferences(selectedGoal, otherGoals);

        if (prefs != null) {
            // TODO: pass prefs to account-creation logic / navigate to next screen
        }
    }

    @FXML
    void initialize() {
        assert this.backButtonLabel != null      : "fx:id=\"backButtonLabel\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.calorieTextField != null     : "fx:id=\"calorieTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.carbsTextField != null       : "fx:id=\"carbsTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.createAccountButton != null  : "fx:id=\"createAccountButton\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.fatTextField != null         : "fx:id=\"fatTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.otherGoalsTextField != null  : "fx:id=\"otherGoalsTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.primaryGoalComboBox != null  : "fx:id=\"primaryGoalComboBox\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.proteinTextField != null     : "fx:id=\"proteinTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.sodiumTextField != null      : "fx:id=\"sodiumTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.sugarTextField != null       : "fx:id=\"sugarTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.warningLabel != null         : "fx:id=\"warningLabel\" was not injected: check your FXML file 'PreferencesPage.fxml'.";

        this.viewModel = new PreferencesVM();

        this.primaryGoalComboBox.getItems().setAll(PrimaryGoal.values());

        NumberStringConverter converter = new NumberStringConverter();
        this.calorieTextField.textProperty().bindBidirectional(this.viewModel.calorieProperty(), converter);
        this.proteinTextField.textProperty().bindBidirectional(this.viewModel.proteinProperty(), converter);
        this.fatTextField.textProperty().bindBidirectional(this.viewModel.fatProperty(), converter);
        this.sugarTextField.textProperty().bindBidirectional(this.viewModel.sugarProperty(), converter);
        this.sodiumTextField.textProperty().bindBidirectional(this.viewModel.sodiumProperty(), converter);
        this.carbsTextField.textProperty().bindBidirectional(this.viewModel.carbsProperty(), converter);

        this.warningLabel.textProperty().bind(this.viewModel.warningProperty());
    }
}