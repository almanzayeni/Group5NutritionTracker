package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositeFoodPageViewModel;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CreateCompositeFoodPageController {
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private Button addFoodButton;
	@FXML
	private Button addIngredientButton;
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
	private ComboBox<QuantityCategory> quantityCategoryComboBox;
	@FXML
	private ListView<FoodItem> selectedIngredientsListView;
	@FXML
	private TextField sodiumTextField;
	@FXML
	private TextField sugarTextField;

	private CreateCompositeFoodPageViewModel viewModel;

	@FXML
	void initialize() {
		assert addFoodButton != null
				: "fx:id=\"addFoodButton\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert addIngredientButton != null
				: "fx:id=\"addIngredientButton\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert caloriesTextField != null
				: "fx:id=\"caloriesTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert carbohydratesTextField != null
				: "fx:id=\"carbohydratesTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert fatTextField != null
				: "fx:id=\"fatTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert hamburgerMenu != null
				: "fx:id=\"hamburgerMenu\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert homeButton != null
				: "fx:id=\"homeButton\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert logoutButton != null
				: "fx:id=\"logoutButton\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert menuPane != null
				: "fx:id=\"menuPane\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert nameTextField != null
				: "fx:id=\"nameTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert portionSizeTextField != null
				: "fx:id=\"portionSizeTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert portionSizeUnitLabel != null
				: "fx:id=\"portionSizeUnitLabel\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert proteinTextField != null
				: "fx:id=\"proteinTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert quantityCategoryComboBox != null
				: "fx:id=\"quantityCategoryComboBox\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert selectedIngredientsListView != null
				: "fx:id=\"selectedIngredientsListView\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert sodiumTextField != null
				: "fx:id=\"sodiumTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert sugarTextField != null
				: "fx:id=\"sugarTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		this.viewModel = new CreateCompositeFoodPageViewModel();
		this.bindViewModel();
		setUpListeners();
	}

	private void setUpListeners() {
		this.setupPortionSizeUnitLabelListener();
		this.handleHamburgerMenuClick();
		// this.setUpListenerForLogoutButton();
		this.setUpListenerForHomeButton();
		// this.setUpListenerForAddIngredientButton();
		this.setupListenerForAddFoodButton();
	}

	private void bindViewModel() {
		this.nameTextField.textProperty().bindBidirectional(this.viewModel.getName());
		this.quantityCategoryComboBox.itemsProperty().bind(this.viewModel.getQuantityCategories());
		this.quantityCategoryComboBox.valueProperty().bindBidirectional(this.viewModel.getSelectedQuantityCategory());
		this.caloriesTextField.textProperty().bind(this.viewModel.getCalories().asString());
		this.proteinTextField.textProperty().bind(this.viewModel.getProtein().asString());
		this.fatTextField.textProperty().bind(this.viewModel.getFat().asString());
		this.sugarTextField.textProperty().bind(this.viewModel.getSugar().asString());
		this.carbohydratesTextField.textProperty().bind(this.viewModel.getCarbohydrates().asString());
		this.sodiumTextField.textProperty().bind(this.viewModel.getSodium().asString());
	}

	private void setupPortionSizeUnitLabelListener() {
		this.quantityCategoryComboBox.setOnAction(event -> {
			QuantityCategory selectedCategory = this.quantityCategoryComboBox.getValue();
			if (selectedCategory != null) {
				switch (selectedCategory) {
				case QUANTITY:
					if (Double.parseDouble(this.portionSizeTextField.getText()) == 1) {
						this.portionSizeUnitLabel.setText("piece");
					} else {
						this.portionSizeUnitLabel.setText("pieces");
					}
					break;
				case WEIGHT:
					if (Double.parseDouble(this.portionSizeTextField.getText()) == 1) {
						this.portionSizeUnitLabel.setText("ounce");
					} else {
						this.portionSizeUnitLabel.setText("ounces");
					}
					break;
				case SERVING:
					if (Double.parseDouble(this.portionSizeTextField.getText()) == 1) {
						this.portionSizeUnitLabel.setText("serving");
					} else {
						this.portionSizeUnitLabel.setText("servings");
					}
					break;
				}
			}
		});
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

//    private void setUpListenerForAddIngredientButton() {
//    	this.addIngredientButton.setOnAction((ActionEvent event) -> {
//			try {
//				FXMLLoader loader = new FXMLLoader();
//				loader.setLocation(/* location of search food page */);
//				loader.load();
//
//				Parent parent = loader.getRoot();
//				Scene scene = new Scene(parent);
//
//				Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
//				stage.setScene(scene);
//				stage.setTitle("Search Food");
//				stage.show();
//
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				Alert alert = new Alert(Alert.AlertType.ERROR, "Error navigating to search page.");
//				alert.showAndWait();
//			}
//    	});
//    }

	private void setupListenerForAddFoodButton() {
		this.addFoodButton.setOnAction((ActionEvent event) -> {
			try {
				this.viewModel.createCompositeFood();
				Alert alert = new Alert(Alert.AlertType.INFORMATION,
						this.nameTextField.getText() + " created successfully.");
				alert.setHeaderText("Food Created");
				alert.showAndWait();
			} catch (Exception ex) {
				if (ex instanceof IllegalArgumentException) {
					Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
					alert.setHeaderText("Food Already Exists");
					alert.showAndWait();
					return;
				}
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error creating " + this.nameTextField.getText()
						+ ". Please ensure all fields are filled out correctly and try again.");
				alert.setHeaderText("Error Creating Food");
				alert.showAndWait();
			}
		});
	}

}
