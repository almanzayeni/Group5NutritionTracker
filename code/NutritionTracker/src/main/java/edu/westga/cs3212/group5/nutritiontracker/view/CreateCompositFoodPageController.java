package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositFoodPageViewModel;

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

public class CreateCompositFoodPageController {
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
	private TextField sodiumTextField;
	@FXML
	private TextField sugarTextField;
	@FXML
	private Label ingredientStatusLabel;
	@FXML
	private FoodSearchPanelController searchPanelController;

	private CreateCompositFoodPageViewModel viewModel;
	
	private FoodItem currentlySelectedFood = null;

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
		setUpListeners();

	       this.viewModel = new CreateCompositFoodPageViewModel();

	        this.setUpQuantityComboBox();
	        this.setUpIngredientsListView();
	        this.connectSearchPanel();
	        this.setUpListeners();

	}
	
    private void connectSearchPanel() {
        this.addIngredientButton.setDisable(true);

        this.searchPanelController.setOnFoodSelected(food -> {
            this.currentlySelectedFood = food;
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
                removeBtn.setOnAction(e -> {
                    int idx = getIndex();
                    if (idx >= 0 && idx < viewModel.getIngredients().size()) {
                        viewModel.removeIngredient(idx);
                    }
                });

                HBox row = new HBox(8, nameLabel, calLabel, removeBtn);
                row.setPadding(new Insets(3, 4, 3, 4));
                setGraphic(row);
                setText(null);
            }
        });
    }

	private void setUpListeners() {
		this.handleHamburgerMenuClick();
		// this.setUpListenerForLogoutButton();
		this.setUpListenerForHomeButton();
		this.setUpListenerForAddIngredientButton();
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

            boolean added = this.viewModel.addIngredient(food);
            if (!added) {
                new Alert(Alert.AlertType.INFORMATION,
                        this.viewModel.statusMessageProperty().get()).showAndWait();
            }
        });
    }

}
