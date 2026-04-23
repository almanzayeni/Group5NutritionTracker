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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CreateFoodItemTypeSelectionPageController implements ViewModelAware {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private JFXHamburger hamburgerMenu;
    @FXML private Button homeButton;
    @FXML private Button createMealButton;
    @FXML private Pane menuPane;
    @FXML private Button selectBaseFoodButton;
    @FXML private Button selectCompositFoodButton;
    @FXML private MenuButton accountMenu;

    private HomeDashboardViewModel viewModel;

    @FXML
    void initialize() {
        this.setUpListeners();
    }
    
    @FXML
    void handleEditDietPlan(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("DietGoalsPage.fxml"));
            loader.load();

            Object controller = loader.getController();
            if (controller instanceof ViewModelAware) {
                ((ViewModelAware) controller).setViewModel(this.viewModel);
            }

            Parent parent = loader.getRoot();
            Stage stage = (Stage) this.accountMenu.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Preferences");
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Unable to open the Preferences page. Please try again.");
            alert.setHeaderText("Navigation Error");
            alert.showAndWait();
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to logout?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("LoginPage.fxml"));
                loader.load();

                Parent parent = loader.getRoot();
                Stage stage = (Stage) this.accountMenu.getScene().getWindow();
                stage.setScene(new Scene(parent));
                stage.setTitle("Login");
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                        "Unable to navigate to the Login page. Please try again.");
                errorAlert.setHeaderText("Navigation Error");
                errorAlert.showAndWait();
            }
        }
    }

    private void setUpListeners() {
        this.handleHamburgerMenuClick();
        this.setUpListenerForHomeButton();
        //this.setUpListenerForCreateMealButton();
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
                    ((ViewModelAware) controller).setViewModel(this.viewModel);
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
    

//    private void setUpListenerForCreateMealButton() {
//        this.createMealButton.setOnAction((ActionEvent event) -> {
//            try {
//                FXMLLoader loader = new FXMLLoader();
//                loader.setLocation(CreateMealItemPageController.class.getResource("CreateMealItemPage.fxml"));
//                loader.load();
//
//                Object controller = loader.getController();
//                if (controller instanceof ViewModelAware) {
//                    ((ViewModelAware) controller).setViewModel(this.viewModel);
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

    private void setUpListenerForSelectBaseFoodButton() {
        this.selectBaseFoodButton.setOnAction((ActionEvent event) -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(
                        CreateBaseFoodPageController.class.getResource("CreateBaseFoodPage.fxml"));
                loader.load();

                Object controller = loader.getController();
                if (controller instanceof ViewModelAware) {
                    ((ViewModelAware) controller).setViewModel(this.viewModel);
                }

                Parent parent = loader.getRoot();
                Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                stage.setScene(new Scene(parent));
                stage.setTitle("Create Base Food Item");
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Unable to open the Create Base Food page. Please try again.");
                alert.setHeaderText("Navigation Error");
                alert.showAndWait();
            }
        });
    }

    private void setUpListenerForSelectCompositeFoodButton() {
        this.selectCompositFoodButton.setOnAction((ActionEvent event) -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(CreateCompositeFoodPageController.class
                        .getResource("CreateCompositeFoodPage.fxml"));
                loader.load();

                Object controller = loader.getController();
                if (controller instanceof ViewModelAware) {
                    ((ViewModelAware) controller).setViewModel(this.viewModel);
                }

                Parent parent = loader.getRoot();
                Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                stage.setScene(new Scene(parent));
                stage.setTitle("Create Composite Food Item");
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Unable to open the Create Composite Food page. Please try again.");
                alert.setHeaderText("Navigation Error");
                alert.showAndWait();
            }
        });
    }

    @Override
    public void setViewModel(HomeDashboardViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
