package edu.westga.cs3212.group5.nutritiontracker.view;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CreateFoodItemTypeSelectionPageController implements ViewModelAware {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private JFXHamburger hamburgerMenu;
    @FXML private Button homeButton;
    @FXML private Button createMealButton;
    @FXML private Button logoutButton;
    @FXML private Pane menuPane;
    @FXML private Button selectBaseFoodButton;
    @FXML private Button selectCompositFoodButton;

    private HomeDashboardViewModel viewModel;

    @FXML
    void initialize() {
        this.setUpListeners();
    }

    private void setUpListeners() {
        this.handleHamburgerMenuClick();
        this.setUpListenerForHomeButton();
		this.setUpListenerForLogoutButton();
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
