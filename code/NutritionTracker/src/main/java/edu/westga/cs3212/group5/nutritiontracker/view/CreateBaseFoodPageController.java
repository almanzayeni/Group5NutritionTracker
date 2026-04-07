package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateBaseFoodPageViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.ViewModelAware;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CreateBaseFoodPageController implements ViewModelAware {
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private Button addFoodButton;
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
	private Button createMealButton;
	@FXML
	private Button logoutButton;
	@FXML
	private Pane menuPane;
	@FXML
	private TextField descriptionTextField;
	@FXML
	private TextField portionSizeTextField;
	@FXML
	private Label portionSizeUnitLabel;
	@FXML
	private TextField proteinTextField;
	@FXML
	private ComboBox<QuantityCategory> quantityCategoryComboBox;
	@FXML
	private TextField sodiumTextField;
	@FXML
	private TextField sugarTextField;

	private CreateBaseFoodPageViewModel viewModel;
	private boolean caloriesIsBound;
	private boolean proteinIsBound;
	private boolean fatIsBound;
	private boolean sugarIsBound;
	private boolean carbohydratesIsBound;
	private boolean sodiumIsBound;
	private HomeDashboardViewModel hdViewModel;

	@FXML
	void initialize() {
		assert addFoodButton != null
				: "fx:id=\"addFoodButton\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert caloriesTextField != null
				: "fx:id=\"caloriesTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert carbohydratesTextField != null
				: "fx:id=\"carbohydratesTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert fatTextField != null
				: "fx:id=\"fatTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert hamburgerMenu != null
				: "fx:id=\"hamburgerMenu\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert homeButton != null
				: "fx:id=\"homeButton\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert createMealButton != null
				: "fx:id=\"createMealButton\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert logoutButton != null
				: "fx:id=\"logoutButton\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert menuPane != null
				: "fx:id=\"menuPane\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert descriptionTextField != null
				: "fx:id=\"descriptionTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert portionSizeTextField != null
				: "fx:id=\"portionSizeTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert portionSizeUnitLabel != null
				: "fx:id=\"portionSizeUnitLabel\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert proteinTextField != null
				: "fx:id=\"proteinTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert quantityCategoryComboBox != null
				: "fx:id=\"quantityCategoryComboBox\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert sodiumTextField != null
				: "fx:id=\"sodiumTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";
		assert sugarTextField != null
				: "fx:id=\"sugarTextField\" was not injected: check your FXML file 'CreateBaseFoodPage.fxml'.";

		this.viewModel = new CreateBaseFoodPageViewModel();
		this.caloriesIsBound = false;
		this.proteinIsBound = false;
		this.fatIsBound = false;
		this.sugarIsBound = false;
		this.carbohydratesIsBound = false;
		this.sodiumIsBound = false;

		setUpListeners();
		bindViewModel();
	}

	private void setUpListeners() {
		this.handleHamburgerMenuClick();
		this.setUpListenerForLogoutButton();
		this.setUpListenerForHomeButton();
		this.setUpListenerForCreateMealButton();
		this.setupPortionSizeUnitLabelListener();
		this.setupCaloriesListener();
		this.setupProteinListener();
		this.setupFatListener();
		this.setupSugarListener();
		this.setupCarbohydratesListener();
		this.setupSodiumListener();
		this.setupListenerForAddFoodButton();
		this.setUpListenerForEnableAddFoodButton();
	}

	private void bindViewModel() {
		this.descriptionTextField.textProperty().bindBidirectional(this.viewModel.getDescriptionProperty());
		this.quantityCategoryComboBox.itemsProperty().bind(this.viewModel.getQuantityCategoriesListProperty());
		this.quantityCategoryComboBox.valueProperty()
				.bindBidirectional(this.viewModel.getSelectedQuantityCategoryProperty());
	}

	private void setupCaloriesListener() {
		this.caloriesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.isBlank()) {
				try {
					if (newValue.endsWith(".")) {
						return;
					}
					double doubleValue = Double.parseDouble(newValue);
					if (!this.caloriesIsBound) {
						this.caloriesTextField.textProperty().bindBidirectional(this.viewModel.getCaloriesProperty(),
								new javafx.util.converter.NumberStringConverter("0.##########"));
						this.caloriesIsBound = true;
					}
					this.viewModel.getCaloriesProperty().setValue(doubleValue);
				} catch (NumberFormatException e) {
					this.caloriesTextField.setText(oldValue);
				}
			}
		});
	}

	private void setupProteinListener() {
		this.proteinTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.isBlank()) {
				try {
					if (newValue.endsWith(".")) {
						return;
					}
					double doubleValue = Double.parseDouble(newValue);
					if (!this.proteinIsBound) {
						this.proteinTextField.textProperty().bindBidirectional(this.viewModel.getProteinProperty(),
								new javafx.util.converter.NumberStringConverter("0.##########"));
						this.proteinIsBound = true;
					}
					this.viewModel.getProteinProperty().setValue(doubleValue);
				} catch (NumberFormatException e) {
					this.proteinTextField.setText(oldValue);
				}
			}
		});
	}

	private void setupFatListener() {
		this.fatTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.isBlank()) {
				try {
					if (newValue.endsWith(".")) {
						return;
					}
					double doubleValue = Double.parseDouble(newValue);
					if (!this.fatIsBound) {
						this.fatTextField.textProperty().bindBidirectional(this.viewModel.getFatProperty(),
								new javafx.util.converter.NumberStringConverter("0.##########"));
						this.fatIsBound = true;
					}
					this.viewModel.getFatProperty().setValue(doubleValue);
				} catch (NumberFormatException e) {
					this.fatTextField.setText(oldValue);
				}
			}
		});
	}

	private void setupSugarListener() {
		this.sugarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.isBlank()) {
				try {
					if (newValue.endsWith(".")) {
						return;
					}
					double doubleValue = Double.parseDouble(newValue);
					if (!this.sugarIsBound) {
						this.sugarTextField.textProperty().bindBidirectional(this.viewModel.getSugarProperty(),
								new javafx.util.converter.NumberStringConverter("0.##########"));
						this.sugarIsBound = true;
					}
					this.viewModel.getSugarProperty().setValue(doubleValue);
				} catch (NumberFormatException e) {
					this.sugarTextField.setText(oldValue);
				}
			}
		});
	}

	private void setupCarbohydratesListener() {
		this.carbohydratesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.isBlank()) {
				try {
					if (newValue.endsWith(".")) {
						return;
					}
					double doubleValue = Double.parseDouble(newValue);
					if (!this.carbohydratesIsBound) {
						this.carbohydratesTextField.textProperty().bindBidirectional(
								this.viewModel.getCarbohydratesProperty(),
								new javafx.util.converter.NumberStringConverter("0.##########"));
						this.carbohydratesIsBound = true;
					}
					this.viewModel.getCarbohydratesProperty().setValue(doubleValue);
				} catch (NumberFormatException e) {
					this.carbohydratesTextField.setText(oldValue);
				}
			}
		});
	}

	private void setupSodiumListener() {
		this.sodiumTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.isBlank()) {
				try {
					if (newValue.endsWith(".")) {
						return;
					}
					double doubleValue = Double.parseDouble(newValue);
					if (!this.sodiumIsBound) {
						this.sodiumTextField.textProperty().bindBidirectional(this.viewModel.getSodiumProperty(),
								new javafx.util.converter.NumberStringConverter("0.##########"));
						this.sodiumIsBound = true;
					}
					this.viewModel.getSodiumProperty().setValue(doubleValue);
				} catch (NumberFormatException e) {
					this.sodiumTextField.setText(oldValue);
				}
			}
		});
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

	private void setUpListenerForLogoutButton() {
		this.logoutButton.setOnAction((ActionEvent event) -> {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(LoginPageController.class.getResource("LoginPage.fxml"));
				loader.load();

				Parent parent = loader.getRoot();
				Scene scene = new Scene(parent);

				Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
				stage.setScene(scene);
				stage.setTitle("Login");
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error logging user out.");
				alert.showAndWait();
			}
		});
	}

	private void setUpListenerForHomeButton() {
		this.homeButton.setOnAction((ActionEvent event) -> {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(HomeDashboardPageController.class.getResource("HomeDashboardPage.fxml"));
				loader.load();

				Parent parent = loader.getRoot();
				Scene scene = new Scene(parent);

				Object controller = loader.getController();
				if (controller instanceof ViewModelAware) {
					((ViewModelAware) controller).setViewModel(this.hdViewModel);
				}

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
				stage.setTitle("Home");
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error navigating to home page.");
				alert.showAndWait();
			}
		});
	}

	private void setupListenerForAddFoodButton() {
		this.addFoodButton.setOnAction((ActionEvent event) -> {
			try {
				this.viewModel.createBaseFood();
				Alert alert = new Alert(Alert.AlertType.INFORMATION,
						this.descriptionTextField.getText() + " created successfully.");
				alert.setHeaderText("Food Created");
				alert.showAndWait();
				this.clearFields();
			} catch (Exception ex) {
				if (ex instanceof IllegalArgumentException) {
					Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
					alert.setHeaderText("Food Already Exists");
					alert.showAndWait();
					return;
				}
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error creating " + this.descriptionTextField.getText()
						+ ". Please ensure all fields are filled out correctly and try again.");
				alert.setHeaderText("Error Creating Food");
				alert.showAndWait();
			}
		});
	}

	private void setUpListenerForEnableAddFoodButton() {
		this.addFoodButton.disableProperty()
				.bind(this.viewModel.getDescriptionProperty().isEmpty()
						.or(this.descriptionTextField.textProperty().isEmpty())
						.or(this.quantityCategoryComboBox.valueProperty().isNull())
						.or(this.quantityCategoryComboBox.getSelectionModel().selectedItemProperty().isNull())
						.or(this.viewModel.getCaloriesProperty().lessThanOrEqualTo(0))
						.or(this.caloriesTextField.textProperty().isEmpty()));
	}

	private void clearFields() {
		this.descriptionTextField.clear();
		this.quantityCategoryComboBox.getSelectionModel().clearSelection();
		this.caloriesTextField.setText("");
		this.proteinTextField.clear();
		this.fatTextField.clear();
		this.sugarTextField.clear();
		this.carbohydratesTextField.clear();
		this.sodiumTextField.clear();
	}

	@Override
	public void setViewModel(HomeDashboardViewModel viewModel) {
		this.hdViewModel = viewModel;
	}

}
