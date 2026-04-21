package edu.westga.cs3212.group5.nutritiontracker.view;

import java.io.IOException;
import java.util.Optional;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.MealType;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.HomeDashboardViewModel;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.ViewModelAware;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Dashboard View Controller.
 *
 * @author vfilpo + Emi + Group 5
 * @version Spring 2026
 */
public class HomeDashboardPageController implements ViewModelAware {
	private HomeDashboardViewModel viewModel;

	@FXML
	private JFXHamburger hamburgerMenu;
	@FXML
	private Pane menuPane;
	@FXML
	private Button homeButton;
	@FXML
	private Button createFoodButton;
	@FXML
	private Button addFoodButton;
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
	private DatePicker calendarPicker;
	@FXML
	private ComboBox<PrimaryGoal> chartGoalComboBox;
	@FXML
	private Label chartGoalLabel;
	@FXML
	private Label goalUnitLabel;
	@FXML
	private TextField remainingGoalValueTextField;
	@FXML
	private PieChart statPieChart;
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
	@FXML
	private Label nameLabel;
	
	@FXML
    void initialize() {
        assert addBreakfastButton != null : "fx:id=\"addBreakfastButton\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert addDinnerButton != null : "fx:id=\"addDinnerButton\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert addFoodButton != null : "fx:id=\"addFoodButton\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert addLunchButton != null : "fx:id=\"addLunchButton\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert addSnacksButton != null : "fx:id=\"addSnacksButton\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert breakfastListView != null : "fx:id=\"breakfastListView\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert calendarPicker != null : "fx:id=\"calendarPicker\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert chartGoalComboBox != null : "fx:id=\"chartGoalComboBox\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert chartGoalLabel != null : "fx:id=\"chartGoalLabel\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert createFoodButton != null : "fx:id=\"createFoodButton\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert dinnerListView != null : "fx:id=\"dinnerListView\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert goalUnitLabel != null : "fx:id=\"goalUnitLabel\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert hamburgerMenu != null : "fx:id=\"hamburgerMenu\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert lunchListView != null : "fx:id=\"lunchListView\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert menuPane != null : "fx:id=\"menuPane\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert nameLabel != null : "fx:id=\"nameLabel\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert remainingGoalValueTextField != null : "fx:id=\"remainingGoalValueTextField\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert snacksListView != null : "fx:id=\"snacksListView\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert statPieChart != null : "fx:id=\"statPieChart\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert todayDateLabel != null : "fx:id=\"todayDateLabel\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
        assert totalCaloriesLabel != null : "fx:id=\"totalCaloriesLabel\" was not injected: check your FXML file 'HomeDashboardPage.fxml'.";
    }

	@Override
	public void setViewModel(HomeDashboardViewModel viewModel) {
		this.viewModel = viewModel;
		this.initialSetup();
	}
	
	private void initialSetup() {
		this.bindProperties();
		this.setupHamburger();
		this.listViewSetup();
		this.setupAddMealButtons();
		this.setupChartGoalComboBoxListener();
		this.setupCalendarPickerListener();
		this.updateChart();
		this.setChartGoalLabels();
		this.updateRemainingGoalValue();
	}

	private void bindProperties() {
		this.totalCaloriesLabel.textProperty()
				.bind(Bindings.format("%.0f kcal", this.viewModel.totalCaloriesProperty()));
		this.calendarPicker.valueProperty().bindBidirectional(this.viewModel.getSelectedDateProperty());
		this.nameLabel.textProperty().bind(this.viewModel.getUsersNameProperty());
		this.todayDateLabel.textProperty().bind(this.viewModel.getDateStringProperty());
		this.breakfastListView.itemsProperty().bind(this.viewModel.getBreakfastItems());
		this.lunchListView.setItems(this.viewModel.getLunchItems());
		this.dinnerListView.setItems(this.viewModel.getDinnerItems());
		this.snacksListView.setItems(this.viewModel.getSnacksItems());
		this.chartGoalComboBox.setItems(FXCollections.observableArrayList(PrimaryGoal.values()));
		this.chartGoalComboBox.valueProperty().bindBidirectional(this.viewModel.selectedGoalProperty());
	}

	private void setupChartGoalComboBoxListener() {
		this.chartGoalComboBox.selectionModelProperty().addListener((obs, oldVal, newVal) -> {
			this.updateChart();
			this.setChartGoalLabels();
			this.updateRemainingGoalValue();
		});
	}

	private void listViewSetup() {
		this.breakfastListView.setCellFactory(lv -> new FoodItemCell(
				item -> this.confirmAndRemove(item, "breakfast", () -> this.viewModel.removeFromBreakfast(item))));
		this.lunchListView.setCellFactory(lv -> new FoodItemCell(
				item -> this.confirmAndRemove(item, "lunch", () -> this.viewModel.removeFromLunch(item))));
		this.dinnerListView.setCellFactory(lv -> new FoodItemCell(
				item -> this.confirmAndRemove(item, "dinner", () -> this.viewModel.removeFromDinner(item))));
		this.snacksListView.setCellFactory(lv -> new FoodItemCell(
				item -> this.confirmAndRemove(item, "snacks", () -> this.viewModel.removeFromSnacks(item))));
		this.bindListViewHeight(this.breakfastListView);
		this.bindListViewHeight(this.lunchListView);
		this.bindListViewHeight(this.dinnerListView);
		this.bindListViewHeight(this.snacksListView);
	}
	
	private void bindListViewHeight(ListView<FoodItem> listView) {
		double cellSize = 35;
		listView.setFixedCellSize(cellSize);
		listView.prefHeightProperty()
				.bind(listView.fixedCellSizeProperty().multiply(Bindings.size(listView.getItems()).add(1)));
	}
	
	private void setupCalendarPickerListener() {
		this.calendarPicker.valueProperty().addListener((obs, oldDate, newDate) -> {
			if (newDate != null) {
				this.viewModel.handleDateChange();
			}
			
			this.updateChart();
			this.updateRemainingGoalValue();
		});
	}

	/**
	 * Wires each meal "+" button to navigate to the Search page with the
	 * appropriate {@link MealType} stored in the VM.
	 */
	private void setupAddMealButtons() {
		this.addBreakfastButton.setOnAction(e -> this.navigateToSearchPage(MealType.BREAKFAST));
		this.addLunchButton.setOnAction(e -> this.navigateToSearchPage(MealType.LUNCH));
		this.addDinnerButton.setOnAction(e -> this.navigateToSearchPage(MealType.DINNER));
		this.addSnacksButton.setOnAction(e -> this.navigateToSearchPage(MealType.SNACKS));
	}

	/**
	 * Stores the target meal in the VM then navigates to the Search page.
	 *
	 * @param mealType the meal the user wants to add food to
	 */
	private void navigateToSearchPage(MealType mealType) {
		this.viewModel.setPendingMealType(mealType);
		this.switchTo("SearchPage.fxml");
	}

	private void confirmAndRemove(FoodItem item, String mealName, Runnable removeAction) {
		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		confirm.setTitle("Remove Food Item");
		confirm.setHeaderText("Remove \"" + item.getDescription() + "\"?");
		confirm.setContentText(
				"Are you sure you want to remove \"" + item.getDescription() + "\" from " + mealName + "?");
		confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

		Optional<ButtonType> result = confirm.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {
			removeAction.run();
		}
	}

	private void setupHamburger() {
		HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(this.hamburgerMenu);
		transition.setRate(-1);

		this.menuPane.setVisible(false);

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
		
		this.homeButton.setOnAction(e -> this.switchTo("HomeDashboardPage.fxml"));
		this.createFoodButton.setOnAction(e -> this.switchTo("CreateFoodItemTypeSelectionPage.fxml"));
		this.logoutButton.setOnAction(e -> this.handleLogout());
	}

	private void handleLogout() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?");
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = alert.showAndWait();

		if (result.isPresent() && result.get() == ButtonType.YES) {
			this.switchTo("LoginPage.fxml");
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

	private void updateChart() {
		PrimaryGoal goal = this.chartGoalComboBox.getValue();
		if (goal == null)
			return;

		var calculations = HomeDashboardViewModel.create(this.viewModel.getCurrentUser(), goal);

		System.out.println("Goal: " + goal);
		System.out.println("Consumed: " + calculations.getConsumedAmount());
		System.out.println("Target: " + calculations.getTargetAmount());
		System.out.println("Remaining: " + calculations.getRemainingAmount());
		System.out.println("Percent used: " + calculations.getPercentUsed());

		var data = FXCollections.observableArrayList(new PieChart.Data("Used", calculations.getConsumedAmount()),
				new PieChart.Data("Remaining", calculations.getRemainingAmount()));

		this.statPieChart.setData(data);
	}

	private void setChartGoalLabels() {
		PrimaryGoal goal = this.chartGoalComboBox.getValue();
		if (goal == null) {
			return;
		}
		this.chartGoalLabel.setText(formatGoal(goal));
		this.goalUnitLabel.setText(getUnit(goal));
	}

	private String formatGoal(PrimaryGoal goal) {
		switch (goal) {
		case CALORIE:
			return "Calories";
		case PROTEIN:
			return "Protein";
		case FAT:
			return "Fat";
		case SUGAR:
			return "Sugar";
		case SODIUM:
			return "Sodium";
		case CARBS:
			return "Carbs";
		case OTHER:
			return "Other";
		default:
			return goal.toString();
		}
	}

	private String getUnit(PrimaryGoal goal) {
		switch (goal) {
		case CALORIE:
			return "cals";
		case PROTEIN:
			return "g";
		case FAT:
			return "g";
		case SUGAR:
			return "g";
		case SODIUM:
			return "mg";
		case CARBS:
			return "g";
		default:
			return "";
		}
	}

	private void updateRemainingGoalValue() {
		PrimaryGoal goal = this.chartGoalComboBox.getValue();
		if (goal == null) {
			return;
		}

		var calculations = HomeDashboardViewModel.create(this.viewModel.getCurrentUser(), goal);

		double remaining = calculations.getRemainingAmount();

		this.remainingGoalValueTextField.setText(String.format("%.1f", remaining));
	}
}
