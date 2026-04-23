package edu.westga.cs3212.group5.nutritiontracker.view;

import java.net.URL;
import java.util.ResourceBundle;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.DietGoalsViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.ViewModelAware;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

/**
 * Controller for the Preferences page.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class DietGoalsController implements ViewModelAware {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField calorieTextField;

    @FXML
    private TextField carbsTextField;

    @FXML
    private Button saveButton;

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

    private DietGoalsViewModel viewModel;
    private HomeDashboardViewModel homeDashboardViewModel;

    @FXML
    void initialize() {
        assert this.calorieTextField != null
                : "fx:id=\"calorieTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.carbsTextField != null
                : "fx:id=\"carbsTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.saveButton != null
                : "fx:id=\"saveButton\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.fatTextField != null
                : "fx:id=\"fatTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.otherGoalsTextField != null
                : "fx:id=\"otherGoalsTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.primaryGoalComboBox != null
                : "fx:id=\"primaryGoalComboBox\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.proteinTextField != null
                : "fx:id=\"proteinTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.sodiumTextField != null
                : "fx:id=\"sodiumTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.sugarTextField != null
                : "fx:id=\"sugarTextField\" was not injected: check your FXML file 'PreferencesPage.fxml'.";
        assert this.warningLabel != null
                : "fx:id=\"warningLabel\" was not injected: check your FXML file 'PreferencesPage.fxml'.";

        this.viewModel = new DietGoalsViewModel();

        this.primaryGoalComboBox.getItems().setAll(PrimaryGoal.values());
        this.primaryGoalComboBox.setValue(PrimaryGoal.CALORIE);

        NumberStringConverter converter = new NumberStringConverter();
        this.calorieTextField.textProperty().bindBidirectional(this.viewModel.calorieProperty(), converter);
        this.proteinTextField.textProperty().bindBidirectional(this.viewModel.proteinProperty(), converter);
        this.fatTextField.textProperty().bindBidirectional(this.viewModel.fatProperty(), converter);
        this.sugarTextField.textProperty().bindBidirectional(this.viewModel.sugarProperty(), converter);
        this.sodiumTextField.textProperty().bindBidirectional(this.viewModel.sodiumProperty(), converter);
        this.carbsTextField.textProperty().bindBidirectional(this.viewModel.carbsProperty(), converter);

        this.warningLabel.textProperty().bind(this.viewModel.warningProperty());
    }

    @FXML
    void handleSave(ActionEvent event) {
        PrimaryGoal selectedGoal = this.primaryGoalComboBox.getValue();
        if (selectedGoal == null) {
            this.viewModel.warningProperty().set("Please select a primary goal.");
            return;
        }

        String otherGoals = this.otherGoalsTextField.getText();
        DietGoals prefs = this.viewModel.createDietGoals(selectedGoal, otherGoals);

        if (prefs == null) {
            return;
        }

        try {
            // TODO: save prefs to the current user/account here

            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeDashboardPage.fxml"));
            Parent parent = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ViewModelAware && this.homeDashboardViewModel != null) {
                ((ViewModelAware) controller).setViewModel(this.homeDashboardViewModel);
            }

            Stage stage = (Stage) this.saveButton.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Home");
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
            this.viewModel.warningProperty().set("Could not return to Home page.");
        }
    }

    @Override
    public void setViewModel(HomeDashboardViewModel viewModel) {
        this.homeDashboardViewModel = viewModel;
    }
}