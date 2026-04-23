package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.ViewModelAware;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller for the standalone Search Food page.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class SearchPageController implements ViewModelAware {

    private HomeDashboardViewModel viewModel;

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private FoodSearchPanelController searchPanelController;

    @FXML private JFXHamburger hamburgerMenu;
    @FXML private Button createFoodButton;
    @FXML private Button homeButton;
    @FXML private Button backButton;
    @FXML private Pane menuPane;
    @FXML private MenuButton accountMenu;

    @FXML
    void initialize() {
        assert this.searchPanelController != null
                : "searchPanelController not injected — check fx:id in FXML";

        this.searchPanelController.enableStandaloneMode();
        this.setUpListeners();
    }
    
    @FXML
    void handleEditDietPlan(ActionEvent event) {
        this.navigateTo("PreferencesPage.fxml");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to logout?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            this.navigateTo("LoginPage.fxml");
        }
    }

    @Override
    public void setViewModel(HomeDashboardViewModel viewModel) {
        this.viewModel = viewModel;

        this.searchPanelController.setOnAddSelectedFood(food -> {
            if (food == null) {
                new Alert(Alert.AlertType.WARNING,
                        "Please select a food from the list first.").showAndWait();
                return;
            }
            this.viewModel.addFoodToPendingMeal(food);
            this.navigateTo("HomeDashboardPage.fxml");
        });
    }

    private void setUpListeners() {
        this.handleHamburgerMenuClick();
        this.setUpListenerForHomeButton();
        this.setUpListenerForBackButton();
        this.setUpListenerForCreateFoodButton();
    }

    private void setUpListenerForCreateFoodButton() {
        this.createFoodButton.setOnAction((ActionEvent event) -> 
            this.navigateTo("CreateFoodItemTypeSelectionPage.fxml")
        );
    }

	private void handleHamburgerMenuClick() {
        HamburgerSlideCloseTransition transition =
                new HamburgerSlideCloseTransition(this.hamburgerMenu);
        transition.setRate(-1);
        this.hamburgerMenu.setOnMouseClicked(event -> {
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
        });
    }

    private void setUpListenerForHomeButton() {
        this.homeButton.setOnAction((ActionEvent event) -> this.navigateTo("HomeDashboardPage.fxml"));
    }

    private void setUpListenerForBackButton() {
        this.backButton.setOnAction((ActionEvent event) -> this.navigateTo("HomeDashboardPage.fxml"));
    }

    private void navigateTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxml));
            loader.load();

            Object controller = loader.getController();
            if (controller instanceof ViewModelAware) {
                ((ViewModelAware) controller).setViewModel(this.viewModel);
            }

            Stage stage = (Stage) this.hamburgerMenu.getScene().getWindow();
            stage.setScene(new Scene(loader.getRoot()));
            stage.setTitle("Home");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error navigating to page.").showAndWait();
        }
    }
}