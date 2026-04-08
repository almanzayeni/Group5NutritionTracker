package edu.westga.cs3212.group5.nutritiontracker.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import edu.westga.cs3212.group5.nutritiontracker.model.User;
import edu.westga.cs3212.group5.nutritiontracker.server.LoginRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;

/**
 * Login page controller.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class LoginPageController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button createAccountButton;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;

    @FXML
    private void handleLogin() {
        this.errorLabel.setStyle("-fx-text-fill: #c0392b;");
        this.errorLabel.setText("");

        String username = this.usernameField.getText().trim();
        String password = this.passwordField.getText().trim();

        if (username.isEmpty()) {
            this.errorLabel.setText("Username is required.");
            return;
        }
        if (password.isEmpty()) {
            this.errorLabel.setText("Password is required.");
            return;
        }

        try {
            String request = LoginRequestHandler.createLoginRequest(username, password);
            User user = LoginRequestHandler.handleLoginRequest(request);
            this.errorLabel.setText("");
            this.switchToDashboard(user);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg != null && msg.toLowerCase().contains("invalid username or password")) {
                this.errorLabel.setText("Invalid username or password. Please try again.");
            } else if (msg != null && msg.toLowerCase().contains("connection")) {
                this.errorLabel.setText("Cannot connect to server. Please ensure the server is running.");
                System.err.println("LoginPageController: server connection error — " + msg);
            } else {
                this.errorLabel.setText("Login failed. Please try again.");
                System.err.println("LoginPageController: login error — " + msg);
            }
        }
    }

    @FXML
    void handleCreateAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccountPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) this.loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Create Account");
            stage.show();
        } catch (IOException e) {
            this.errorLabel.setStyle("-fx-text-fill: #c0392b;");
            this.errorLabel.setText("Unable to open the account creation page.");
            System.err.println("LoginPageController: navigation error — " + e.getMessage());
        }
    }

    private void switchToDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeDashboardPage.fxml"));
            Parent root = loader.load();

            HomeDashboardPageController controller = loader.getController();
            HomeDashboardViewModel vm = new HomeDashboardViewModel(user);
            controller.setViewModel(vm);

            Stage stage = (Stage) this.loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home Dashboard");
            stage.show();
        } catch (IOException e) {
            this.errorLabel.setStyle("-fx-text-fill: #c0392b;");
            this.errorLabel.setText("Navigation error: " + e.getMessage());
            System.err.println("LoginPageController: dashboard navigation error — " + e.getMessage());
        }
    }

    @FXML
    void initialize() {
        assert this.createAccountButton != null
                : "fx:id=\"createAccountButton\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert this.errorLabel != null
                : "fx:id=\"errorLabel\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert this.loginButton != null
                : "fx:id=\"loginButton\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert this.passwordField != null
                : "fx:id=\"passwordField\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert this.usernameField != null
                : "fx:id=\"usernameField\" was not injected: check your FXML file 'LoginPage.fxml'.";

        // Allow Enter key to trigger login from either field
        this.usernameField.setOnAction(e -> this.handleLogin());
        this.passwordField.setOnAction(e -> this.handleLogin());
    }
}