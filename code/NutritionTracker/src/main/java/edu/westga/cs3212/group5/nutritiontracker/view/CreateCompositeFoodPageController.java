package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositeFoodPageViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.ViewModelAware;

import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class CreateCompositeFoodPageController implements ViewModelAware {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button addFoodButton;
    @FXML private Button addIngredientButton;
    @FXML private TextField caloriesTextField;
    @FXML private TextField carbohydratesTextField;
    @FXML private TextField fatTextField;
    @FXML private JFXHamburger hamburgerMenu;
    @FXML private Button homeButton;
    @FXML private Button createMealButton;
    @FXML private Button logoutButton;
    @FXML private Pane menuPane;
    @FXML private TextField descriptionTextField;
    @FXML private TextField portionSizeTextField;
    @FXML private Label portionSizeUnitLabel;
    @FXML private TextField proteinTextField;
    @FXML private ComboBox<QuantityCategory> quantityCategoryComboBox;
    @FXML private ListView<FoodItem> selectedIngredientsListView;
    @FXML private Label searchSelectionLabel;
    @FXML private Label ingredientStatusLabel;
    @FXML private TextField sodiumTextField;
    @FXML private TextField sugarTextField;

    @FXML private FoodSearchPanelController searchPanelController;

    private CreateCompositeFoodPageViewModel viewModel;
    private HomeDashboardViewModel hdViewModel;

    @FXML
    void initialize() {
        this.viewModel = new CreateCompositeFoodPageViewModel();
        this.searchPanelController.enableIngredientMode();
        this.connectSearchPanel();
        this.setUpIngredientsListView();
        this.bindViewModel();
        this.setUpListeners();
    }

    private void connectSearchPanel() {
        this.searchPanelController.setOnFoodSelected(food -> {
            boolean hasSelection = food != null;
            this.addIngredientButton.setDisable(!hasSelection);
            if (this.searchSelectionLabel != null) {
                this.searchSelectionLabel.setText(hasSelection
                        ? "Selected: " + food.getDescription() + " — "
                          + String.format("%.0f", food.getCalories()) + " cal"
                        : "No food selected from search.");
            }
        });
    }

    @Override
    public void setViewModel(HomeDashboardViewModel viewModel) {
        this.hdViewModel = viewModel;
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
                removeBtn.setOnAction(event -> confirmAndRemoveIngredient(item));

                HBox row = new HBox(8, descriptionLabel, calLabel, removeBtn);
                row.setPadding(new Insets(3, 4, 3, 4));
                setGraphic(row);
                setText(null);
            }
        });
    }
    
    private void confirmAndRemoveIngredient(FoodItem item) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Ingredient");
        confirm.setHeaderText("Remove \"" + item.getDescription() + "\"?");
        confirm.setContentText("Are you sure you want to remove \""
                + item.getDescription() + "\" from this food?");
        confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                this.viewModel.removeIngredient(item);
                if (this.ingredientStatusLabel != null) {
                    this.ingredientStatusLabel.setText("\"" + item.getDescription() + "\" removed.");
                }
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.setHeaderText("Remove Error");
                alert.showAndWait();
            }
        }
    }

    private void setUpListeners() {
        this.setupPortionSizeUnitLabelListener();
        this.handleHamburgerMenuClick();
        this.setUpListenerForHomeButton();
        this.setUpListenerForLogoutButton();
        //this.setUpListenerForCreateMealButton();
        this.setupListenerForAddFoodButton();
        this.setUpListenerForAddIngredientButton();
        this.setUpListenerForEnableAddFoodButton();
    }

    private void bindViewModel() {
        this.descriptionTextField.textProperty()
                .bindBidirectional(this.viewModel.getDescriptionProperty());
        this.quantityCategoryComboBox.itemsProperty()
                .bind(this.viewModel.getQuantityCategoriesListPropery());
        this.quantityCategoryComboBox.valueProperty()
                .bindBidirectional(this.viewModel.getSelectedQuantityCategoryProperty());
        this.selectedIngredientsListView.itemsProperty()
                .bindBidirectional(this.viewModel.getIngredientsListProperty());
        this.caloriesTextField.textProperty()
                .bind(this.viewModel.getTotalCaloriesProperty().asString());
        this.proteinTextField.textProperty()
                .bind(this.viewModel.getTotalProteinProperty().asString());
        this.fatTextField.textProperty()
                .bind(this.viewModel.getTotalFatProperty().asString());
        this.sugarTextField.textProperty()
                .bind(this.viewModel.getTotalSugarProperty().asString());
        this.carbohydratesTextField.textProperty()
                .bind(this.viewModel.getTotalCarbohydratesProperty().asString());
        this.sodiumTextField.textProperty()
                .bind(this.viewModel.getTotalSodiumProperty().asString());
    }

    private void setupPortionSizeUnitLabelListener() {
        this.quantityCategoryComboBox.setOnAction(event -> {
            QuantityCategory selectedCategory = this.quantityCategoryComboBox.getValue();
            if (selectedCategory != null) {
                switch (selectedCategory) {
                    case QUANTITY -> this.portionSizeUnitLabel.setText(
                            Double.parseDouble(this.portionSizeTextField.getText()) == 1 ? "piece" : "pieces");
                    case WEIGHT -> this.portionSizeUnitLabel.setText(
                            Double.parseDouble(this.portionSizeTextField.getText()) == 1 ? "ounce" : "ounces");
                    case SERVING -> this.portionSizeUnitLabel.setText(
                            Double.parseDouble(this.portionSizeTextField.getText()) == 1 ? "serving" : "servings");
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
                if (this.menuPane.isVisible()) {
                    this.menuPane.setVisible(false);
                    this.homeButton.disableProperty().set(true);
                } else {
                    this.menuPane.setVisible(true);
                    this.homeButton.disableProperty().set(false);
                    this.menuPane.toFront();
                    this.hamburgerMenu.toFront();
                    this.homeButton.toFront();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setUpListenerForHomeButton() {
        this.homeButton.setOnAction((ActionEvent event) -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(HomeDashboardPageController.class.getResource("HomeDashboardPage.fxml"));
                loader.load();

                Object controller = loader.getController();
                if (controller instanceof ViewModelAware) {
                    ((ViewModelAware) controller).setViewModel(this.hdViewModel);
                }

                Parent parent = loader.getRoot();
                Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                stage.setScene(new Scene(parent));
                stage.setTitle("Home");
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Unable to navigate to the home page. Please try again.");
                alert.setHeaderText("Navigation Error");
                alert.showAndWait();
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
	
//    private void setUpListenerForCreateMealButton() {
//        this.createMealButton.setOnAction((ActionEvent event) -> {
//            try {
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(CreateMealItemPageController.class.getResource("CreateMealItemPage.fxml"));
//                loader.load();
//
//                Object controller = loader.getController();
//                if (controller instanceof ViewModelAware) {
//                    ((ViewModelAware) controller).setViewModel(this.hdViewModel);
//                }
//
//                Parent parent = loader.getRoot();
//                Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
//                stage.setScene(new Scene(parent));
//                stage.setTitle("Create Meal");
//                stage.show();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Alert alert = new Alert(Alert.AlertType.ERROR,
//                        "Unable to navigate to the Create Meal page. Please try again.");
//                alert.setHeaderText("Navigation Error");
//                alert.showAndWait();
//            }
//        });
//    }

    private void setUpListenerForAddIngredientButton() {
        this.addIngredientButton.setOnAction((ActionEvent event) -> {
            FoodItem food = this.searchPanelController.getSelectedFood();

            if (food == null) {
                new Alert(Alert.AlertType.WARNING,
                        "Please select a food from the search results before adding it as an ingredient.")
                        .showAndWait();
                return;
            }

            try {
                food.setPortionSize(this.searchPanelController.getSelectedPortionSize());
                this.viewModel.addIngredient(food);
                this.searchPanelController.reset();
                this.addIngredientButton.setDisable(true);
                if (this.searchSelectionLabel != null) {
                    this.searchSelectionLabel.setText("No food selected from search.");
                }
                if (this.ingredientStatusLabel != null) {
                    this.ingredientStatusLabel.setText("\"" + food.getDescription() + "\" added as ingredient.");
                }
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
                alert.setHeaderText("Could Not Add Ingredient");
                alert.showAndWait();
            }
        });
    }

    private void setupListenerForAddFoodButton() {
        this.addFoodButton.setOnAction((ActionEvent event) -> {
            try {
                this.viewModel.createCompositeFood();
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "\"" + this.descriptionTextField.getText() + "\" was created successfully.");
                alert.setHeaderText("Food Created");
                alert.showAndWait();
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.setHeaderText("Validation Error");
                alert.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Could not create \"" + this.descriptionTextField.getText()
                        + "\". Please ensure all fields are filled and try again.");
                alert.setHeaderText("Error Creating Food");
                alert.showAndWait();
            }
        });
    }

    private void setUpListenerForEnableAddFoodButton() {
        this.addFoodButton.disableProperty()
                .bind(this.viewModel.getDescriptionProperty().isEmpty()
                        .or(this.quantityCategoryComboBox.valueProperty().isNull())
                        .or(this.viewModel.getIngredientsListProperty().emptyProperty()));
    }

}