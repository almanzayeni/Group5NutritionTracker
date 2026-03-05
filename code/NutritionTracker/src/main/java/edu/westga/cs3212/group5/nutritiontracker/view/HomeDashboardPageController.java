package edu.westga.cs3212.group5.nutritiontracker.view;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.binding.Bindings;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Dashboard View
 * 
 * @author vfilpo + Emi :)
 */
public class HomeDashboardPageController {
	@FXML
    private Button calendarButton;

    @FXML
    private JFXHamburger hamburgerMenu;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;
    
    @FXML
    private Button addBreakfastButton;
    
    @FXML
    private Button addLunchButton;
    
    @FXML
    private Button addDinnerButton;
    
    @FXML
    private Button addSnacksButton;
    
    @FXML
    private Button createFoodButton;
    
    @FXML
    private Button addFoodButton;
    
    @FXML
    private DatePicker calendarPicker;
    
    @FXML
    private Pane menuPane;
    
    @FXML
    private ListView<FoodItem> breakfastListView;
    
    @FXML
    private ListView<FoodItem> lunchListView;
    
    @FXML
    private ListView<FoodItem> dinnerListView;
    
    @FXML
    private ListView<FoodItem> snacksListView;
    
    @FXML
    private Label todayDateLabel;
    
    @FXML
    private Label totalCaloriesLabel;
    
    @FXML private void goHome() { switchTo("HomeDashboardPage.fxml"); }

    @FXML private void goCreateFood() { switchTo("CreateFoodItemTypeSelectionPage.fxml"); }

    @FXML private void goAddFood() { switchTo("SearchPage.fxml"); }
    
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("M-d-yyyy");
    
    private HomeDashboardViewModel viewModel;
   
    /**
     * Sets the ViewModel that data will be pulled from.
	 * After calling this method, the {@code calendarPicker.valueProperty()} will be
	 * bound bidirectionally to the ViewModel's {@code selectedDateProperty}, 
	 * ensuring that changes in either the UI or the ViewModel are reflected in both.
     * 
     * @param the HomeDashboardViewModel instance to bind to this controller
     */
    private void setViewModel(HomeDashboardViewModel viewModel) {
        this.viewModel = viewModel;
        bindProperties();
    }

    private void bindProperties() {
        this.totalCaloriesLabel.textProperty().bind(Bindings.format("%.0f kcal", this.viewModel.totalCaloriesProperty()));
        calendarPicker.valueProperty().bindBidirectional(viewModel.selectedDateProperty());
        //TODO: remove
        System.out.println(this.calendarPicker.getValue());
        
    }

    @FXML
    private void initialize() {
    	this.setViewModel(new HomeDashboardViewModel());
    	this.setupDateLabel();
    	
    	this.handleHamburgerMenuClick();
    	
    	this.breakfastListView.setItems(this.viewModel.getBreakfastItems());
    	this.lunchListView.setItems(this.viewModel.getLunchItems());
    	this.dinnerListView.setItems(this.viewModel.getDinnerItems());
    	this.snacksListView.setItems(this.viewModel.getSnacksItems());
    	
    	this.breakfastListView.setCellFactory(lv -> new FoodItemCell(item -> viewModel.removeFromBreakfast(item)));
        this.lunchListView.setCellFactory(lv -> new FoodItemCell(item -> viewModel.removeFromLunch(item)));
        this.dinnerListView.setCellFactory(lv -> new FoodItemCell(item -> viewModel.removeFromDinner(item)));
        this.snacksListView.setCellFactory(lv -> new FoodItemCell(item -> viewModel.removeFromSnacks(item)));
            
        // Uncomment when we have AddFood page
//        this.addBreakfastButton.setOnAction(e -> this.goToAddFood("Breakfast"));
//        this.addLunchButton.setOnAction(e -> this.goToAddFood("Lunch"));
//        this.addDinnerButton.setOnAction(e -> this.goToAddFood("Dinner"));
//        this.addSnacksButton.setOnAction(e -> this.goToAddFood("Snacks"));
        //TODO: remove
        /*
         * Below is how we will add event listeners to make the UI reactive:
         * 
         * viewModel.selectedDateProperty().addListener((obs, oldDate, newDate) -> {...});
         */
    }
    
    private static String formatDate(LocalDate date) {
        return date == null ? "" : DATE_FMT.format(date);
    }
    
    private void setupDateLabel() {
        todayDateLabel.setText(DATE_FMT.format(viewModel.getSelectedDate()));

        viewModel.selectedDateProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                todayDateLabel.setText(DATE_FMT.format(newDate));
            } else {
                todayDateLabel.setText("");
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
    
    // For once we have add food page.
//    private void goToAddFood() {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                getClass().getResource("")
//            );
//            Parent root = loader.load();
//
//            Stage stage = (Stage) addBreakfastButton.getScene().getWindow();
//            stage.setScene(new Scene(root));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
    
    private void switchTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();

            Stage stage = (Stage) this.hamburgerMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FunctionalInterface
    private interface RemoveHandler {
        void remove(FoodItem item);
    }
    
}
