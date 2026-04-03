package edu.westga.cs3212.group5.nutritiontracker.view;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.server.CreateAccountRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for the Create Account page.
 *
 * @author Yeni A
 * @version Spring 2026
 */
public class CreateAccountPageController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<PrimaryGoal> primaryGoalComboBox;
    @FXML private TextField calorieField;
    @FXML private TextField proteinField;
    @FXML private TextField carbsField;
    @FXML private TextField sugarField;
    @FXML private TextField sodiumField;
    @FXML private TextField fatField;
    @FXML private Label errorLabel;
    @FXML private Button createAccountButton;
    @FXML private Button backToLoginButton;

    @FXML
    void initialize() {
        assert this.usernameField != null;
        assert this.passwordField != null;
        assert this.primaryGoalComboBox != null;
        assert this.calorieField != null;
        assert this.proteinField != null;
        assert this.carbsField != null;
        assert this.sugarField != null;
        assert this.sodiumField != null;
        assert this.fatField != null;
        assert this.errorLabel != null;
        assert this.createAccountButton != null;
        assert this.backToLoginButton != null;

        this.primaryGoalComboBox.getItems().setAll(PrimaryGoal.values());
    }

    /**
     * Handles Create Account button click
     */
    @FXML
    void handleCreateAccount() {
        String username = this.usernameField.getText().trim();
        String password = this.passwordField.getText().trim();
        PrimaryGoal goal = this.primaryGoalComboBox.getValue();

        // Basic validation
        if (username.isEmpty() || password.isEmpty()) {
            this.errorLabel.setText("Please enter a username and password.");
            return;
        }

        if (goal == null) {
            this.errorLabel.setText("Please select a primary goal.");
            return;
        }

        // 🔥 NEW: Enforce selected goal field
        if (!this.isPrimaryGoalFieldFilled(goal)) {
            this.errorLabel.setText("Please enter a value for " + goal.toString().toLowerCase() + ".");
            return;
        }

        DietGoals dietGoals;
        try {
            dietGoals = this.buildDietGoals(goal);
        } catch (NumberFormatException e) {
            this.errorLabel.setText("Goal fields must be valid numbers.");
            return;
        } catch (IllegalArgumentException e) {
            this.errorLabel.setText(e.getMessage());
            return;
        }

        try {
            String request = CreateAccountRequestHandler.createCreateAccountRequest(
                    username, password, username, dietGoals);

            User user = CreateAccountRequestHandler.handleCreateAccountRequest(request);

            this.errorLabel.setText("");
            this.switchToDashboard(user);

        } catch (Exception e) {
            this.errorLabel.setText("Account creation failed: " + e.getMessage());
        }
    }

    /**
     * 🔥 NEW: Validates only the selected goal field is filled
     */
    private boolean isPrimaryGoalFieldFilled(PrimaryGoal goal) {
        switch (goal) {
            case CALORIE:
                return !this.calorieField.getText().trim().isEmpty();
            case PROTEIN:
                return !this.proteinField.getText().trim().isEmpty();
            case FAT:
                return !this.fatField.getText().trim().isEmpty();
            case SUGAR:
                return !this.sugarField.getText().trim().isEmpty();
            case SODIUM:
                return !this.sodiumField.getText().trim().isEmpty();
            case CARBS:
                return !this.carbsField.getText().trim().isEmpty();
            case OTHER:
                return true;
            default:
                return false;
        }
    }

    @FXML
    void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) this.backToLoginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            this.errorLabel.setText("Navigation error: " + e.getMessage());
        }
    }

    private DietGoals buildDietGoals(PrimaryGoal primaryGoal) {
        double calories = parseOptionalDouble(this.calorieField.getText());
        double protein = parseOptionalDouble(this.proteinField.getText());
        double carbs = parseOptionalDouble(this.carbsField.getText());
        double sugar = parseOptionalDouble(this.sugarField.getText());
        double sodium = parseOptionalDouble(this.sodiumField.getText());
        double fat = parseOptionalDouble(this.fatField.getText());

        return new DietGoals(primaryGoal, calories, protein, fat, sugar, sodium, carbs,
                Collections.emptyList());
    }

    private static double parseOptionalDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(text.trim());
    }

    private void switchToDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeDashboardPage.fxml"));
            Parent root = loader.load();

            HomeDashboardPageController controller = loader.getController();
            HomeDashboardViewModel vm = new HomeDashboardViewModel(user);
            controller.setViewModel(vm);

            Stage stage = (Stage) this.createAccountButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            this.errorLabel.setText("Navigation error: " + e.getMessage());
        }
    }
}