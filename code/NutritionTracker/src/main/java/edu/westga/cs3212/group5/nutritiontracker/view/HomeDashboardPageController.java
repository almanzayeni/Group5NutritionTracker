package edu.westga.cs3212.group5.nutritiontracker.view;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.ViewModelAware;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Dashboard View Controller
 *
 * @author vfilpo + Emi
 * @version Spring 2026
 */
public class HomeDashboardPageController implements ViewModelAware {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("M-d-yyyy");

    private HomeDashboardViewModel viewModel;

    @FXML private JFXHamburger hamburgerMenu;
    @FXML private Pane menuPane;
    @FXML private Button homeButton;
    @FXML private Button createFoodButton;
    @FXML private Button addFoodButton;
    @FXML private Button logoutButton;
    @FXML private Button addBreakfastButton;
    @FXML private Button addLunchButton;
    @FXML private Button addDinnerButton;
    @FXML private Button addSnacksButton;
    @FXML private DatePicker calendarPicker;
    @FXML private ListView<FoodItem> breakfastListView;
    @FXML private ListView<FoodItem> lunchListView;
    @FXML private ListView<FoodItem> dinnerListView;
    @FXML private ListView<FoodItem> snacksListView;
    @FXML private Label todayDateLabel;
    @FXML private Label totalCaloriesLabel;
    @FXML private Label nameLabel;

    @Override
    public void setViewModel(HomeDashboardViewModel viewModel) {
        this.viewModel = viewModel;
        this.bindProperties();
    }

    private void bindProperties() {
        this.totalCaloriesLabel.textProperty()
                .bind(Bindings.format("%.0f kcal", this.viewModel.totalCaloriesProperty()));
        this.calendarPicker.valueProperty()
                .bindBidirectional(this.viewModel.getSelectedDateProperty());
        this.nameLabel.textProperty().bind(this.viewModel.getUsersNameProperty());

        this.setupDateLabel();

        this.breakfastListView.setItems(this.viewModel.getBreakfastItems());
        this.lunchListView.setItems(this.viewModel.getLunchItems());
        this.dinnerListView.setItems(this.viewModel.getDinnerItems());
        this.snacksListView.setItems(this.viewModel.getSnacksItems());

        this.breakfastListView.setCellFactory(lv ->
                new FoodItemCell(item -> this.confirmAndRemove(item, "breakfast",
                        () -> this.viewModel.removeFromBreakfast(item))));
        this.lunchListView.setCellFactory(lv ->
                new FoodItemCell(item -> this.confirmAndRemove(item, "lunch",
                        () -> this.viewModel.removeFromLunch(item))));
        this.dinnerListView.setCellFactory(lv ->
                new FoodItemCell(item -> this.confirmAndRemove(item, "dinner",
                        () -> this.viewModel.removeFromDinner(item))));
        this.snacksListView.setCellFactory(lv ->
                new FoodItemCell(item -> this.confirmAndRemove(item, "snacks",
                        () -> this.viewModel.removeFromSnacks(item))));

        this.bindListViewHeight(this.breakfastListView);
        this.bindListViewHeight(this.lunchListView);
        this.bindListViewHeight(this.dinnerListView);
        this.bindListViewHeight(this.snacksListView);
    }

    private void setupDateLabel() {
        this.todayDateLabel.setText(DATE_FMT.format(this.viewModel.getSelectedDate()));
        this.viewModel.getSelectedDateProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                this.todayDateLabel.setText(DATE_FMT.format(newDate));
            } else {
                this.todayDateLabel.setText("");
            }
        });
    }

    private void bindListViewHeight(ListView<FoodItem> listView) {
        double cellSize = 35;
        listView.setFixedCellSize(cellSize);
        listView.prefHeightProperty().bind(
            listView.fixedCellSizeProperty().multiply(Bindings.size(listView.getItems()).add(1))
        );
    }

    private void confirmAndRemove(FoodItem item, String mealName, Runnable removeAction) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Food Item");
        confirm.setHeaderText("Remove \"" + item.getDescription() + "\"?");
        confirm.setContentText("Are you sure you want to remove \"" + item.getDescription() + "\" from " + mealName + "?");
        confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            removeAction.run();
        }
    }

    @FXML
    private void initialize() {
        this.setupHamburger();
        this.setupMenuButtons();
    }

    private void setupHamburger() {
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(this.hamburgerMenu);
        transition.setRate(-1);

        this.menuPane.setVisible(false); // start hidden

        this.hamburgerMenu.setOnMouseClicked(event -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            boolean show = !this.menuPane.isVisible();
            this.menuPane.setVisible(show);

            if (show) {
                this.menuPane.toFront();
                this.hamburgerMenu.toFront();
            }
        });
    }

    private void setupMenuButtons() {
        homeButton.setOnAction(e -> this.switchTo("HomeDashboardPage.fxml"));
        createFoodButton.setOnAction(e -> this.switchTo("CreateFoodItemTypeSelectionPage.fxml"));
        //addFoodButton.setOnAction(e -> this.switchTo("SearchPage.fxml"));
        logoutButton.setOnAction(e -> this.handleLogout());
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            this.switchTo("loginPage.fxml");
        }
    }

    private void switchTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ViewModelAware) {
                ((ViewModelAware) controller).setViewModel(this.viewModel);
            }

            Stage stage = (Stage) this.hamburgerMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error navigating to page: " + e.getMessage());
            alert.setHeaderText("Navigation Error");
            alert.showAndWait();
        }
    }
}