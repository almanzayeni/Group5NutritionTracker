package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateBaseFoodPageViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.ViewModelAware;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;

import javafx.beans.property.DoubleProperty;
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

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button addFoodButton;
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
    @FXML private TextField sodiumTextField;
    @FXML private TextField sugarTextField;

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
        this.viewModel = new CreateBaseFoodPageViewModel();
        this.caloriesIsBound = false;
        this.proteinIsBound = false;
        this.fatIsBound = false;
        this.sugarIsBound = false;
        this.carbohydratesIsBound = false;
        this.sodiumIsBound = false;

        this.setUpListeners();
        this.bindViewModel();
    }

    private void setUpListeners() {
        this.handleHamburgerMenuClick();
        this.setUpListenerForHomeButton();
        this.setUpListenerForCreateMealButton();
        this.setupPortionSizeUnitLabelListener();
        //this.setUpListenerForLogoutButton();
        this.setupNumericFieldListener(this.caloriesTextField,
                this.viewModel.getCaloriesProperty(), () -> this.caloriesIsBound,
                () -> this.caloriesIsBound = true,
                this.viewModel.getCaloriesProperty());
        this.setupProteinListener();
        this.setupFatListener();
        this.setupSugarListener();
        this.setupCarbohydratesListener();
        this.setupSodiumListener();
        this.setupListenerForAddFoodButton();
        this.setUpListenerForEnableAddFoodButton();
    }

    private void bindViewModel() {
        this.descriptionTextField.textProperty()
                .bindBidirectional(this.viewModel.getDescriptionProperty());
        this.quantityCategoryComboBox.itemsProperty()
                .bind(this.viewModel.getQuantityCategoriesListProperty());
        this.quantityCategoryComboBox.valueProperty()
                .bindBidirectional(this.viewModel.getSelectedQuantityCategoryProperty());
    }
    
    private void setupNumericFieldListener(TextField field,
            DoubleProperty prop,
            BooleanSupplier isBound,
            Runnable setBound,
            DoubleProperty bindTarget) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isBlank()) {
                try {
                    if (newVal.endsWith(".")) {
                        return;
                    }
                    double val = Double.parseDouble(newVal);
                    if (val < 0) {
                        this.showInlineError(field, oldVal, "Value cannot be negative.");
                        return;
                    }
                    if (!isBound.getAsBoolean()) {
                        field.textProperty().bindBidirectional(prop,
                                new javafx.util.converter.NumberStringConverter("0.##########"));
                        setBound.run();
                    }
                    bindTarget.setValue(val);
                } catch (NumberFormatException e) {
                    this.showInlineError(field, oldVal, "Please enter a valid number.");
                }
            }
        });
    }
    
    private void showInlineError(TextField field, String oldVal, String message) {
        field.setText(oldVal);
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.setHeaderText("Invalid Input");
        alert.showAndWait();
    }

    private void setupCaloriesListener() {
        this.caloriesTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isBlank()) {
                try {
                    if (newValue.endsWith(".")) {
                        return;
                    }
                    double doubleValue = Double.parseDouble(newValue);
                    if (doubleValue < 0) {
                        this.caloriesTextField.setText(oldValue);
                        new Alert(Alert.AlertType.WARNING, "Calories cannot be negative.")
                                .showAndWait();
                        return;
                    }
                    if (!this.caloriesIsBound) {
                        this.caloriesTextField.textProperty().bindBidirectional(
                                this.viewModel.getCaloriesProperty(),
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
                    if (doubleValue < 0) {
                        this.proteinTextField.setText(oldValue);
                        new Alert(Alert.AlertType.WARNING, "Protein cannot be negative.")
                                .showAndWait();
                        return;
                    }
                    if (!this.proteinIsBound) {
                        this.proteinTextField.textProperty().bindBidirectional(
                                this.viewModel.getProteinProperty(),
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
                    if (doubleValue < 0) {
                        this.fatTextField.setText(oldValue);
                        new Alert(Alert.AlertType.WARNING, "Fat cannot be negative.")
                                .showAndWait();
                        return;
                    }
                    if (!this.fatIsBound) {
                        this.fatTextField.textProperty().bindBidirectional(
                                this.viewModel.getFatProperty(),
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
                    if (doubleValue < 0) {
                        this.sugarTextField.setText(oldValue);
                        new Alert(Alert.AlertType.WARNING, "Sugar cannot be negative.")
                                .showAndWait();
                        return;
                    }
                    if (!this.sugarIsBound) {
                        this.sugarTextField.textProperty().bindBidirectional(
                                this.viewModel.getSugarProperty(),
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
                    if (doubleValue < 0) {
                        this.carbohydratesTextField.setText(oldValue);
                        new Alert(Alert.AlertType.WARNING, "Carbohydrates cannot be negative.")
                                .showAndWait();
                        return;
                    }
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
                    if (doubleValue < 0) {
                        this.sodiumTextField.setText(oldValue);
                        new Alert(Alert.AlertType.WARNING, "Sodium cannot be negative.")
                                .showAndWait();
                        return;
                    }
                    if (!this.sodiumIsBound) {
                        this.sodiumTextField.textProperty().bindBidirectional(
                                this.viewModel.getSodiumProperty(),
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

//    private void setUpListenerForLogoutButton() {
// 		this.logoutButton.setOnAction((ActionEvent event) -> {
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

    private void setUpListenerForCreateMealButton() {
        this.createMealButton.setOnAction((ActionEvent event) -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(CreateMealItemPageController.class.getResource("CreateMealItemPage.fxml"));
                loader.load();

                Object controller = loader.getController();
                if (controller instanceof ViewModelAware) {
                    ((ViewModelAware) controller).setViewModel(this.hdViewModel);
                }

                Parent parent = loader.getRoot();
                Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                stage.setScene(new Scene(parent));
                stage.setTitle("Create Meal");
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Unable to navigate to the Create Meal page. Please try again.");
                alert.setHeaderText("Navigation Error");
                alert.showAndWait();
            }
        });
    }

    private void setupListenerForAddFoodButton() {
        this.addFoodButton.setOnAction((ActionEvent event) -> {
            try {
                this.viewModel.createBaseFood();
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "\"" + this.descriptionTextField.getText() + "\" was created successfully.");
                alert.setHeaderText("Food Created");
                alert.showAndWait();
                this.clearFields();
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.setHeaderText("Validation Error");
                alert.showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Could not create \"" + this.descriptionTextField.getText()
                        + "\". Please ensure all fields are filled out correctly and try again.");
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
