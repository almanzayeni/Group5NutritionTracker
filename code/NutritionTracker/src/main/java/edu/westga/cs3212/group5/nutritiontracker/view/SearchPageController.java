package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;

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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller for the standalone Search Food page.
 *
 * To reuse on another page, simply add:
 *   fx:include fx:id="searchPanel" source="FoodSearchPanel.fxml" />
 * and inject @FXML private FoodSearchPanelController searchPanelController;
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class SearchPageController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private FoodSearchPanelController searchPanelController;

    @FXML private Button addSelectedFoodButton;
    @FXML private Label selectionLabel;
    @FXML private JFXHamburger hamburgerMenu;
    @FXML private Button homeButton;
    @FXML private Button logoutButton;
    @FXML private Pane menuPane;

    private FoodItem currentlySelectedFood = null;

    @FXML
    void initialize() {
        assert this.searchPanelController != null : "searchPanelController not injected — check fx:id in FXML";
        assert this.addSelectedFoodButton != null;
        assert this.selectionLabel != null;

        this.searchPanelController.setOnFoodSelected(food -> {
            this.currentlySelectedFood = food;
            this.selectionLabel.setText("Selected: " + food.getDescription()
                    + " — " + String.format("%.0f", food.getCalories()) + " cal");
        });

        this.addSelectedFoodButton.setDisable(true);
        this.searchPanelController.setOnFoodSelected(food -> {
            this.currentlySelectedFood = food;
            boolean hasSelection = food != null;
            this.addSelectedFoodButton.setDisable(!hasSelection);
            this.selectionLabel.setText(hasSelection
                    ? "Selected: " + food.getDescription()
                      + " — " + String.format("%.0f", food.getCalories()) + " cal"
                    : "No food selected.");
        });

        this.setUpListeners();
    }

    @FXML
    private void handleAddSelectedFood(ActionEvent event) {
        if (this.currentlySelectedFood == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a food from the list first.").showAndWait();
            return;
        }

        // TODO: wire up to meal log / daily tracker in a future sprint
        new Alert(Alert.AlertType.INFORMATION,
                "\"" + this.currentlySelectedFood.getDescription()
                + "\" added! (Persistence hook — wire up in next sprint.)")
                .showAndWait();

        this.searchPanelController.reset();
        this.currentlySelectedFood = null;
        this.addSelectedFoodButton.setDisable(true);
        this.selectionLabel.setText("No food selected.");
    }

    private void setUpListeners() {
        this.handleHamburgerMenuClick();
        this.setUpListenerForHomeButton();
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

                Parent parent = loader.getRoot();
                Scene scene = new Scene(parent);

                Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                stage.setScene(scene);
                stage.setTitle("Home");
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error navigating to home page.").showAndWait();
            }
        });
    }
}