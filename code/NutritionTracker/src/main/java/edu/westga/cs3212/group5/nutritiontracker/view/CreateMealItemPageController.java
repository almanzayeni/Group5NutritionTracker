package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateMealItemPageViewModel;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class CreateMealItemPageController {
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private Button addMealButton;
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
	private Button createFoodButton;
	@FXML
	private Button logoutButton;
	@FXML
	private Pane menuPane;
	@FXML
	private TextField descriptionTextField;
	@FXML
	private TextField proteinTextField;
	@FXML
	private ListView<FoodItem> selectedIngredientsListView;
	@FXML
	private Label searchSelectionLabel;
	@FXML
	private Label ingredientStatusLabel;
	@FXML
	private TextField sodiumTextField;
	@FXML
	private TextField sugarTextField;

	private CreateMealItemPageViewModel viewModel;
	private FoodSearchPanelController searchPanelController;

	@FXML
	void initialize() {
		assert addMealButton != null
				: "fx:id=\"addMealButton\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert addFoodButton != null
				: "fx:id=\"addFoodButton\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
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
		assert createFoodButton != null
				: "fx:id=\"createFoodButton\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert logoutButton != null
				: "fx:id=\"logoutButton\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert menuPane != null
				: "fx:id=\"menuPane\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert descriptionTextField != null
				: "fx:id=\"descriptionTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert proteinTextField != null
				: "fx:id=\"proteinTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert selectedIngredientsListView != null
				: "fx:id=\"selectedIngredientsListView\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert sodiumTextField != null
				: "fx:id=\"sodiumTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		assert sugarTextField != null
				: "fx:id=\"sugarTextField\" was not injected: check your FXML file 'CreateCompositFoodPage.fxml'.";
		this.viewModel = new CreateMealItemPageViewModel();
		this.searchPanelController = new FoodSearchPanelController();
		this.connectSearchPanel();
		this.setUpIngredientsListView();
		this.bindViewModel();
		setUpListeners();
	}

	private void connectSearchPanel() {
		this.searchPanelController.setOnFoodSelected(food -> {
			boolean hasSelection = food != null;
			this.addFoodButton.setDisable(!hasSelection);
			if (this.searchSelectionLabel != null) {
				this.searchSelectionLabel
						.setText(hasSelection
								? "Selected: " + food.getDescription() + " — "
										+ String.format("%.0f", food.getCalories()) + " cal"
								: "No food selected from search.");
			}
		});
	}

	private void setUpIngredientsListView() {
		this.selectedIngredientsListView.setCellFactory(lv -> new ListCell<>() {
			@Override
			protected void updateItem(FoodItem item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setGraphic(null);
					setText(null);
					return;
				}

				Label descriptionLabel = new Label(item.getDescription());
				descriptionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
				HBox.setHgrow(descriptionLabel, Priority.ALWAYS);

				Label calLabel = new Label(String.format("%.0f cal", item.getCalories()));
				calLabel.setStyle("-fx-text-fill: #555555; -fx-font-size: 12;");

				Button removeBtn = new Button("✕");
				removeBtn.setStyle("-fx-background-color: #e07070; -fx-text-fill: white; "
						+ "-fx-font-weight: bold; -fx-padding: 2 7 2 7;");
				removeBtn.setOnAction(event -> {
					viewModel.removeFood(item);
				});

				HBox row = new HBox(8, descriptionLabel, calLabel, removeBtn);
				row.setPadding(new Insets(3, 4, 3, 4));
				setGraphic(row);
				setText(null);
			}
		});
	}

	private void setUpListeners() {
		this.handleHamburgerMenuClick();
		this.setUpListenerForLogoutButton();
		this.setUpListenerForHomeButton();
		this.setUpListenerForCreateFoodButton();
		this.setupListenerForAddMealButton();
		this.setUpListenerForAddFoodButton();
		this.setUpListenerForEnableAddMealButton();
	}

	private void bindViewModel() {
		this.descriptionTextField.textProperty().bindBidirectional(this.viewModel.getDescriptionProperty());
		this.selectedIngredientsListView.itemsProperty().bindBidirectional(this.viewModel.getIngredientsListProperty());
		this.caloriesTextField.textProperty().bind(this.viewModel.getTotalCaloriesProperty().asString());
		this.proteinTextField.textProperty().bind(this.viewModel.getTotalProteinProperty().asString());
		this.fatTextField.textProperty().bind(this.viewModel.getTotalFatProperty().asString());
		this.sugarTextField.textProperty().bind(this.viewModel.getTotalSugarProperty().asString());
		this.carbohydratesTextField.textProperty().bind(this.viewModel.getTotalCarbohydratesProperty().asString());
		this.sodiumTextField.textProperty().bind(this.viewModel.getTotalSodiumProperty().asString());
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

	private void setUpListenerForCreateFoodButton() {
		this.createFoodButton.setOnAction((ActionEvent event) -> {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(CreateFoodItemTypeSelectionPageController.class
						.getResource("CreateFoodItemTypeSelectionPage.fxml"));
				loader.load();

				Parent parent = loader.getRoot();
				Scene scene = new Scene(parent);

				Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
				stage.setScene(scene);
				stage.setTitle("Select Food Type");
				stage.show();

			} catch (Exception ex) {
				ex.printStackTrace();
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error navigating to create food item selection page.");
				alert.showAndWait();
			}
		});
	}

	private void setUpListenerForAddFoodButton() {
		this.addFoodButton.setOnAction((ActionEvent event) -> {
			FoodItem food = this.searchPanelController.getSelectedFood();

			if (food == null) {
				new Alert(Alert.AlertType.WARNING, "Please select a food from the search results first.").showAndWait();
				return;
			}

			try {
				this.viewModel.addFood(food);
			} catch (IllegalArgumentException e) {
				new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
			}
		});
	}

	private void setupListenerForAddMealButton() {
		this.addMealButton.setOnAction((ActionEvent event) -> {
			try {
				this.viewModel.createMealItem();
				Alert alert = new Alert(Alert.AlertType.INFORMATION,
						this.descriptionTextField.getText() + " created successfully.");
				alert.setHeaderText("Meal Created");
				alert.showAndWait();
			} catch (Exception ex) {
				if (ex instanceof IllegalArgumentException) {
					Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
					alert.setHeaderText("Meal Already Exists");
					alert.showAndWait();
					return;
				}
				Alert alert = new Alert(Alert.AlertType.ERROR, "Error creating " + this.descriptionTextField.getText()
						+ ". Please ensure all fields are filled out correctly and try again.");
				alert.setHeaderText("Error Creating Meal");
				alert.showAndWait();
			}
		});
	}

	private void setUpListenerForEnableAddMealButton() {
		this.addMealButton.disableProperty().bind(this.viewModel.getDescriptionProperty().isEmpty()
				.or(this.viewModel.getIngredientsListProperty().emptyProperty()));
	}

}
