package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
	private Button createMealButton;
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
		assert hamburgerMenu != null
				: "fx:id=\"hamburgerMenu\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
		assert homeButton != null
				: "fx:id=\"homeButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
		assert createMealButton != null
				: "fx:id=\"createMealButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
		assert logoutButton != null
				: "fx:id=\"logoutButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
		assert menuPane != null
				: "fx:id=\"menuPane\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
		assert selectBaseFoodButton != null
				: "fx:id=\"selectBaseFoodButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
		assert selectCompositFoodButton != null
				: "fx:id=\"selectCompositFoodButton\" was not injected: check your FXML file 'CreateFoodItemTypeSelectionPage.fxml'.";
		setUpListeners();
	}

	private void setUpListeners() {
		this.handleHamburgerMenuClick();
		// this.setUpListenerForLogoutButton();
		this.setUpListenerForHomeButton();
		this.setUpListenerForCreateMealButton();
		this.setUpListenerForSelectBaseFoodButton();
		this.setUpListenerForSelectCompositeFoodButton();
	}

	private void handleHamburgerMenuClick() {
		HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(this.hamburgerMenu);
		transition.setRate(-1);
		this.hamburgerMenu.setOnMouseClicked(event -> {
			try {
				transition.setRate(transition.getRate() * -1);
				transition.play();
				if (menuPane.isVisible()) {
					menuPane.setVisible(false);
					homeButton.disableProperty().set(true);
				} else {
					menuPane.setVisible(true);
					homeButton.disableProperty().set(false);
					menuPane.toFront();
					hamburgerMenu.toFront();
					homeButton.toFront();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

//    private void setUpListenerForLogoutButton() {
//		this.logoutButton.setOnAction((ActionEvent event) -> {
//			try {
//				//this.viewModel.saveData();
//
//				FXMLLoader loader = new FXMLLoader();
//				loader.setLocation(LandingPage.class.getResource("LoginView.fxml"));
//				loader.load();
//
//				Parent parent = loader.getRoot();
//				Scene scene = new Scene(parent);
//
//				Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
//				stage.setScene(scene);
//				stage.setTitle("Login");
//				stage.show();
//
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				Alert alert = new Alert(Alert.AlertType.ERROR, "Error logging user out.");
//				alert.showAndWait();
//			}
//		});
//	}

	private void setUpListenerForHomeButton() {
		this.homeButton.setOnAction((ActionEvent event) -> {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(HomeDashboardPageController.class.getResource("HomeDashboardPage.fxml"));
				loader.load();

				Parent parent = loader.getRoot();
				Scene scene = new Scene(parent);

				Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
				stage.setScene(scene);
				stage.setTitle("Home");
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error navigating to home page.");
				alert.showAndWait();
			}
		});
	}
	
	private void setUpListenerForCreateMealButton() {
		this.createMealButton.setOnAction((ActionEvent event) -> {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(CreateMealItemPageController.class.getResource("CreateMealItemPage.fxml"));
				loader.load();

				Parent parent = loader.getRoot();
				Scene scene = new Scene(parent);

				Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
				stage.setScene(scene);
				stage.setTitle("Ceate Meal");
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error navigating to home page.");
				alert.showAndWait();
			}
		});
	}

	private void setUpListenerForSelectBaseFoodButton() {
		this.selectBaseFoodButton.setOnAction((ActionEvent event) -> {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(CreateBaseFoodPageController.class.getResource("CreateBaseFoodPage.fxml"));
				loader.load();

				Parent parent = loader.getRoot();
				Scene scene = new Scene(parent);

				Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
				stage.setScene(scene);
				stage.setTitle("Create Base Food Item");
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error navigating to create base food item page.");
				alert.showAndWait();
			}
		});
	}

	private void setUpListenerForSelectCompositeFoodButton() {
		this.selectCompositFoodButton.setOnAction((ActionEvent event) -> {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(CreateCompositeFoodPageController.class.getResource("CreateCompositeFoodPage.fxml"));
				loader.load();

				Parent parent = loader.getRoot();
				Scene scene = new Scene(parent);

				Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
				stage.setScene(scene);
				stage.setTitle("Create Base Food Item");
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error navigating to create base food item page.");
				alert.showAndWait();
			}
		});
	}

}
