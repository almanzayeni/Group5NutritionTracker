package edu.westga.cs3212.group5.nutritiontracker.view;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.server.EditDietGoalsHandler;
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
 * Controller for the Diet Goals page.
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

    private void populateFieldsFromCurrentUser() {
        if (this.homeDashboardViewModel == null || this.homeDashboardViewModel.getCurrentUser() == null) {
            return;
        }

        DietGoals goals = this.homeDashboardViewModel.getCurrentUser().getDietGoals();
        if (goals == null) {
            return;
        }

        this.primaryGoalComboBox.setValue(goals.getPrimaryGoal());
        this.viewModel.calorieProperty().set(goals.getCalorieGoal());
        this.viewModel.proteinProperty().set(goals.getProteinGoal());
        this.viewModel.fatProperty().set(goals.getFatGoal());
        this.viewModel.sugarProperty().set(goals.getSugarGoal());
        this.viewModel.sodiumProperty().set(goals.getSodiumGoal());
        this.viewModel.carbsProperty().set(goals.getCarbsGoal());

        String otherGoalsText = goals.getOtherGoals()
                .stream()
                .collect(Collectors.joining(", "));
        this.otherGoalsTextField.setText(otherGoalsText);
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
        this.homeButton.setOnAction(event -> this.navigateTo("HomeDashboardPage.fxml", "Home"));
    }

    private void setUpListenerForCreateFoodButton() {
        this.createFoodButton.setOnAction(event -> this.navigateTo("CreateFoodItemTypeSelectionPage.fxml", "Create Food"));
    }

    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            this.navigateToLogin();
        }
    }

    @FXML
    void handleSave(ActionEvent event) {
        PrimaryGoal selectedGoal = this.primaryGoalComboBox.getValue();
        if (selectedGoal == null) {
            this.viewModel.warningProperty().set("Please select a primary goal.");
            return;
        }

        DietGoals prefs = this.viewModel.createDietGoals(selectedGoal, this.otherGoalsTextField.getText());
        if (prefs == null) {
            return;
        }

        if (this.homeDashboardViewModel == null || this.homeDashboardViewModel.getCurrentUser() == null) {
            this.viewModel.warningProperty().set("Could not find current user.");
            return;
        }

        DietGoals oldGoals = this.homeDashboardViewModel.getCurrentUser().getDietGoals();

        try {
            this.homeDashboardViewModel.updateDietGoals(prefs);

            String request = EditDietGoalsHandler.createEditDietGoalsRequest(
                    this.homeDashboardViewModel.getCurrentUser());
            EditDietGoalsHandler.handleEditDietGoalsRequest(request);

            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Diet Goals Saved");
            confirmation.setHeaderText("Diet goals have been saved.");
            confirmation.setContentText("Your updated diet goals were saved successfully.");
            confirmation.showAndWait();

            this.navigateTo("HomeDashboardPage.fxml", "Home");

        } catch (Exception ex) {
            ex.printStackTrace();
            this.homeDashboardViewModel.updateDietGoals(oldGoals);
            this.viewModel.warningProperty().set("Could not save diet goals.");
        }
    }

    private void navigateTo(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ViewModelAware && this.homeDashboardViewModel != null) {
                ((ViewModelAware) controller).setViewModel(this.homeDashboardViewModel);
            }

            Stage stage = (Stage) this.hamburgerMenu.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle(title);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
            this.viewModel.warningProperty().set("Could not open page.");
        }
    }

    private void navigateToLogin() {
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

    @Override
    public void setViewModel(HomeDashboardViewModel viewModel) {
        this.homeDashboardViewModel = viewModel;
        this.populateFieldsFromCurrentUser();
    }
}