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

/**
 * Login page controller
 * 
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class LoginPageController {
	
	// TODO: remove before server auth is wired up
	private static final String DEFAULT_USERNAME = "nutri";
	private static final String DEFAULT_PASSWORD = "pass123";

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button createAccountButton;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private void handleLogin() {
        String username = this.usernameField.getText().trim();
        String password = this.passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            this.errorLabel.setText("Please enter your username and password.");
            return;
        }

        // TODO: Replace this stub with actual server authentication:
        //
        //   try {
        //       LoginRequest request = new LoginRequest(username, password);
        //       LoginResponse response = new ServerClient().send(request, LoginResponse.class);
        //       if (!response.isSuccess()) {
        //           this.errorLabel.setText("Invalid username or password.");
        //           return;
        //       }
        //   } catch (Exception e) {
        //       this.errorLabel.setText("Could not connect to server.");
        //       return;
        //   }

        if (!username.equals(DEFAULT_USERNAME) || !password.equals(DEFAULT_PASSWORD)) {
            this.errorLabel.setText("Invalid username or password.");
            return;
        }
        
        this.errorLabel.setText("");
        this.switchTo("HomeDashboardPage.fxml");
    }

    @FXML
    void handleCreateAccount() {
        // TODO: this.switchTo("CreateAccountPage.fxml");
        this.errorLabel.setText("Account creation coming soon!");
        this.errorLabel.setStyle("-fx-text-fill: #1f5c33; -fx-font-size: 12px;");
    }
    
    private void switchTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) this.loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
            this.errorLabel.setText("Navigation error: " + exception.getMessage());
        }
    }

    @FXML
    void initialize() {
        assert this.createAccountButton != null : "fx:id=\"createAccountButton\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert this.errorLabel != null : "fx:id=\"errorLabel\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert this.loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert this.passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert this.usernameField != null : "fx:id=\"usernameField\" was not injected: check your FXML file 'LoginPage.fxml'.";

    }

}
