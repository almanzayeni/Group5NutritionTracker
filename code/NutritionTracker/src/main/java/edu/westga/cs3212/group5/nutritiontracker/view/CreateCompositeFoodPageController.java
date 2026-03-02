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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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
    private Label searchSelectionLabel;
	@FXML
	private Label ingredientStatusLabel;
	@FXML
	private TextField sodiumTextField;
	@FXML
	private TextField sugarTextField;

	private CreateCompositeFoodPageViewModel viewModel;
	private FoodSearchPanelController searchPanelController;

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
		this.searchPanelController = new FoodSearchPanelController();
		this.connectSearchPanel();
		this.setUpIngredientsListView();
		this.bindViewModel();
		setUpListeners();
	}
	
	private void connectSearchPanel() {
        this.searchPanelController.setOnFoodSelected(food -> {
            boolean hasSelection = food != null;
            this.addIngredientButton.setDisable(!hasSelection);
            if (this.searchSelectionLabel != null) {
                this.searchSelectionLabel.setText(hasSelection
                        ? "Selected: " + food.getDescription()
                          + " — " + String.format("%.0f", food.getCalories()) + " cal"
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

                Label nameLabel = new Label(item.getDescription());
                nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
                HBox.setHgrow(nameLabel, Priority.ALWAYS);

                Label calLabel = new Label(String.format("%.0f cal", item.getCalories()));
                calLabel.setStyle("-fx-text-fill: #555555; -fx-font-size: 12;");

                Button removeBtn = new Button("✕");
                removeBtn.setStyle(
                        "-fx-background-color: #e07070; -fx-text-fill: white; "
                        + "-fx-font-weight: bold; -fx-padding: 2 7 2 7;");
                removeBtn.setOnAction(event -> {
                    viewModel.removeIngredient(item);
                });

                HBox row = new HBox(8, nameLabel, calLabel, removeBtn);
                row.setPadding(new Insets(3, 4, 3, 4));
                setGraphic(row);
                setText(null);
            }
        });
    }

	private void setUpListeners() {
		this.setupPortionSizeUnitLabelListener();
		this.handleHamburgerMenuClick();
		// this.setUpListenerForLogoutButton();
		this.setUpListenerForHomeButton();
		this.setupListenerForAddFoodButton();
		this.setUpListenerForAddIngredientButton();
		this.setUpListenerForEnableAddFoodButton();
	}

	private void bindViewModel() {
		this.nameTextField.textProperty().bindBidirectional(this.viewModel.getNameProperty());
		this.quantityCategoryComboBox.itemsProperty().bind(this.viewModel.getQuantityCategoriesListPropery());
		this.quantityCategoryComboBox.valueProperty().bindBidirectional(this.viewModel.getSelectedQuantityCategoryProperty());
		this.selectedIngredientsListView.itemsProperty().bindBidirectional(this.viewModel.getIngredientsListProperty());
		this.caloriesTextField.textProperty().bind(this.viewModel.getTotalCaloriesProperty().asString());
		this.proteinTextField.textProperty().bind(this.viewModel.getTotalProteinProperty().asString());
		this.fatTextField.textProperty().bind(this.viewModel.getTotalFatProperty().asString());
		this.sugarTextField.textProperty().bind(this.viewModel.getTotalSugarProperty().asString());
		this.carbohydratesTextField.textProperty().bind(this.viewModel.getTotalCarbohydratesProperty().asString());
		this.sodiumTextField.textProperty().bind(this.viewModel.getTotalSodiumProperty().asString());
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
	
	private void setUpListenerForAddIngredientButton() {
        this.addIngredientButton.setOnAction((ActionEvent event) -> {
            FoodItem food = this.searchPanelController.getSelectedFood();

            if (food == null) {
                new Alert(Alert.AlertType.WARNING,
                        "Please select a food from the search results first.").showAndWait();
                return;
            }

            try {
				this.viewModel.addIngredient(food);
			} catch (IllegalArgumentException e) {
				new Alert(Alert.AlertType.WARNING, e.getMessage()).showAndWait();
			}
        });
    }

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
	
	private void setUpListenerForEnableAddFoodButton() {
		this.addFoodButton.disableProperty().bind(
				this.viewModel.getNameProperty().isEmpty()
				.or(this.quantityCategoryComboBox.valueProperty().isNull())
				.or(this.viewModel.getIngredientsListProperty().emptyProperty())
		);
	}

}
