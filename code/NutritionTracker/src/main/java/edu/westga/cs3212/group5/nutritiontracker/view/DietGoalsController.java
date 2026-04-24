package edu.westga.cs3212.group5.nutritiontracker.view;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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
    @FXML
    private JFXHamburger hamburgerMenu;

    @FXML
    private Pane menuPane;

    @FXML
    private Button homeButton;

    @FXML
    private Button createFoodButton;

    @FXML
    private MenuButton accountMenu;

    private DietGoalsViewModel viewModel;
    private HomeDashboardViewModel homeDashboardViewModel;

    @FXML
    void initialize() {
        assert this.calorieTextField != null
                : "fx:id=\"calorieTextField\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.carbsTextField != null
                : "fx:id=\"carbsTextField\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.saveButton != null
                : "fx:id=\"saveButton\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.fatTextField != null
                : "fx:id=\"fatTextField\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.otherGoalsTextField != null
                : "fx:id=\"otherGoalsTextField\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.primaryGoalComboBox != null
                : "fx:id=\"primaryGoalComboBox\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.proteinTextField != null
                : "fx:id=\"proteinTextField\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.sodiumTextField != null
                : "fx:id=\"sodiumTextField\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.sugarTextField != null
                : "fx:id=\"sugarTextField\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.warningLabel != null
                : "fx:id=\"warningLabel\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.hamburgerMenu != null
                : "fx:id=\"hamburgerMenu\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.menuPane != null
                : "fx:id=\"menuPane\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.homeButton != null
                : "fx:id=\"homeButton\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.createFoodButton != null
                : "fx:id=\"createFoodButton\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";
        assert this.accountMenu != null
                : "fx:id=\"accountMenu\" was not injected: check your FXML file 'DietGoalsPage.fxml'.";

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
        this.setUpListeners();
    }
    
    private void setUpListeners() {
        this.handleHamburgerMenuClick();
        this.setUpListenerForHomeButton();
        this.setUpListenerForCreateFoodButton();
    }
    
    private void handleHamburgerMenuClick() {
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(this.hamburgerMenu);
        transition.setRate(-1);
        this.menuPane.setVisible(false);

        this.hamburgerMenu.setOnMouseClicked(event -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            boolean show = !this.menuPane.isVisible();
            this.menuPane.setVisible(show);

            if (show) {
                this.menuPane.toFront();
                this.hamburgerMenu.toFront();
            }
        });
    }
    
    private void setUpListenerForHomeButton() {
        this.homeButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeDashboardPage.fxml"));
                Parent parent = loader.load();

                Object controller = loader.getController();
                if (controller instanceof ViewModelAware && this.homeDashboardViewModel != null) {
                    ((ViewModelAware) controller).setViewModel(this.homeDashboardViewModel);
                }

                Stage stage = (Stage) this.hamburgerMenu.getScene().getWindow();
                stage.setScene(new Scene(parent));
                stage.setTitle("Home");
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
                this.viewModel.warningProperty().set("Could not return to Home page.");
            }
        });
    }
    
    private void setUpListenerForCreateFoodButton() {
        this.createFoodButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateFoodItemTypeSelectionPage.fxml"));
                Parent parent = loader.load();

                Object controller = loader.getController();
                if (controller instanceof ViewModelAware && this.homeDashboardViewModel != null) {
                    ((ViewModelAware) controller).setViewModel(this.homeDashboardViewModel);
                }

                Stage stage = (Stage) this.hamburgerMenu.getScene().getWindow();
                stage.setScene(new Scene(parent));
                stage.setTitle("Create Food");
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
                this.viewModel.warningProperty().set("Could not open Create Food page.");
            }
        });
    }
    
    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
                Parent parent = loader.load();

                Stage stage = (Stage) this.accountMenu.getScene().getWindow();
                stage.setScene(new Scene(parent));
                stage.setTitle("Login");
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                this.viewModel.warningProperty().set("Could not return to Login page.");
            }
        }
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