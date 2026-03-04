package edu.westga.cs3212.group5.nutritiontracker.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodDatabase;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodDatabase.SortOption;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Controller for the reusable FoodSearchPanel component..
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class FoodSearchPanelController {

    @FXML private URL location;
    @FXML private ResourceBundle resources;

    @FXML private TextField searchTextField;
    @FXML private ComboBox<SortOption> sortComboBox;
    @FXML private ListView<FoodItem> resultsListView;
    @FXML private Label statusLabel;

    @FXML private HBox portionSizeRow;
    @FXML private Spinner<Double> portionSizeSpinner;
    @FXML private Label portionUnitLabel;

    @FXML private Label selectionLabel;
    @FXML private Button addSelectedFoodButton;

    private Consumer<FoodItem> onFoodSelected;
    private FoodItem currentlySelectedFood = null;

    @FXML
    void initialize() {
        assert this.searchTextField != null  : "fx:id=\"searchTextField\" not injected";
        assert this.sortComboBox != null     : "fx:id=\"sortComboBox\" not injected";
        assert this.resultsListView != null  : "fx:id=\"resultsListView\" not injected";
        assert this.statusLabel != null      : "fx:id=\"statusLabel\" not injected";

        this.sortComboBox.getItems().setAll(SortOption.values());
        this.sortComboBox.setValue(SortOption.NAME_ASC);

        this.resultsListView.setCellFactory(lv -> new FoodCell());

        this.searchTextField.textProperty()
                .addListener((obs, oldVal, newVal) -> this.refreshResults());

        this.sortComboBox.valueProperty()
                .addListener((obs, oldVal, newVal) -> this.refreshResults());

        this.resultsListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    this.currentlySelectedFood = newVal;

                    if (this.selectionLabel != null && this.selectionLabel.isVisible()) {
                        boolean hasSelection = newVal != null;
                        this.selectionLabel.setText(hasSelection
                                ? "Selected: " + newVal.getDescription()
                                  + " — " + String.format("%.0f", newVal.getCalories()) + " cal"
                                : "No food selected.");
                        if (this.addSelectedFoodButton != null) {
                            this.addSelectedFoodButton.setDisable(!hasSelection);
                        }
                    }
                    if (this.portionSizeRow != null && this.portionSizeRow.isVisible() && newVal != null) {
                        this.updatePortionUnitLabel(newVal);
                    }

                    if (newVal != null && this.onFoodSelected != null) {
                        this.onFoodSelected.accept(newVal);
                    }
                });

        this.refreshResults();
    }

    /**
     * Switches the panel into ingredient mode by showing the portion size
     * spinner row. Call this from CreateCompositeFoodPageController after load.
     * The spinner resets to 1 whenever a new food is selected.
     */
    public void enableIngredientMode() {
        if (this.portionSizeRow != null) {
            this.portionSizeRow.setVisible(true);
            this.portionSizeRow.setManaged(true);
        }
        if (this.portionSizeSpinner != null) {
            this.portionSizeSpinner.setValueFactory(
                    new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 9999, 1, 1));
            this.portionSizeSpinner.setEditable(true);
        }
    }

    /**
     * Switches this panel into standalone page mode by making the selection
     * label and Add button visible.
     */
    public void enableStandaloneMode() {
        if (this.selectionLabel != null) {
            this.selectionLabel.setVisible(true);
            this.selectionLabel.setManaged(true);
        }
        if (this.addSelectedFoodButton != null) {
            this.addSelectedFoodButton.setVisible(true);
            this.addSelectedFoodButton.setManaged(true);
            this.addSelectedFoodButton.setDisable(true);
            this.addSelectedFoodButton.setOnAction(this::handleAddSelectedFood);
        }
    }

    /**
     * Registers a callback that fires whenever the user selects a food item.
     *
     * @param callback receives the selected FoodItem; may be null to clear
     */
    public void setOnFoodSelected(Consumer<FoodItem> callback) {
        this.onFoodSelected = callback;
    }

    /**
     * Returns the currently highlighted food, or null if nothing is selected.
     *
     * @return the selected FoodItem, or null
     */
    public FoodItem getSelectedFood() {
        return this.resultsListView.getSelectionModel().getSelectedItem();
    }

    /**
     * Returns the portion size currently shown in the spinner.
     * Defaults to 1.0 if ingredient mode is not enabled.
     *
     * @return the portion size value
     */
    public double getSelectedPortionSize() {
        if (this.portionSizeSpinner == null || !this.portionSizeRow.isVisible()) {
            return 1.0;
        }
        try {
            this.portionSizeSpinner.commitValue();
            return this.portionSizeSpinner.getValue();
        } catch (Exception e) {
            return 1.0;
        }
    }

    /**
     * Clears the search text, resets the portion spinner to 1, and reloads results.
     */
    public void reset() {
        this.searchTextField.clear();
        this.sortComboBox.setValue(SortOption.NAME_ASC);
        this.currentlySelectedFood = null;
        if (this.portionSizeSpinner != null && this.portionSizeRow.isVisible()) {
            this.portionSizeSpinner.getValueFactory().setValue(1.0);
            this.portionUnitLabel.setText("");
        }
        if (this.selectionLabel != null && this.selectionLabel.isVisible()) {
            this.selectionLabel.setText("No food selected.");
        }
        if (this.addSelectedFoodButton != null) {
            this.addSelectedFoodButton.setDisable(true);
        }
        this.refreshResults();
    }

    /**
     * Exposes the status label text property so the host can bind or update it.
     *
     * @return the status label text property
     */
    public StringProperty statusTextProperty() {
        return this.statusLabel.textProperty();
    }

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

        this.reset();
    }

    private void updatePortionUnitLabel(FoodItem food) {
        if (this.portionUnitLabel == null) {
            return;
        }
        QuantityCategory cat = food.getQuantityCategory();
        if (cat == null) {
            this.portionUnitLabel.setText("");
            return;
        }
        switch (cat) {
            case QUANTITY -> this.portionUnitLabel.setText("piece(s)");
            case WEIGHT   -> this.portionUnitLabel.setText("g");
            case SERVING  -> this.portionUnitLabel.setText("serving(s)");
        }
    }

    private void refreshResults() {
        String query = this.searchTextField.getText();
        SortOption sort = this.sortComboBox.getValue();

        List<FoodItem> results = FoodDatabase.getInstance().search(query, sort);
        this.resultsListView.setItems(FXCollections.observableArrayList(results));

        if (results.isEmpty()) {
            this.statusLabel.setText("No foods found matching \"" + query + "\".");
        } else {
            this.statusLabel.setText(results.size()
                    + " result(s) — click to select, then press the Add button.");
        }
    }

    private static class FoodCell extends ListCell<FoodItem> {

        @Override
        protected void updateItem(FoodItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            Label nameLabel = new Label(item.getDescription());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
            HBox.setHgrow(nameLabel, Priority.ALWAYS);

            Label calLabel = new Label(String.format("%.0f cal", item.getCalories()));
            calLabel.setStyle("-fx-text-fill: #555555; -fx-font-size: 12;");

            String catText = "—";
            if (item instanceof BaseFood bf) {
                catText = bf.getQuantityCategory().toString();
            } else if (item instanceof CompositeFood cf) {
                catText = cf.getQuantityCategory().toString();
            }
            Label catLabel = new Label(catText);
            catLabel.setStyle(
                    "-fx-background-color: #b8d5a3; -fx-padding: 2 6 2 6; "
                    + "-fx-background-radius: 4; -fx-font-size: 11;");

            HBox row = new HBox(8, nameLabel, calLabel, catLabel);
            row.setPadding(new Insets(4, 2, 4, 2));
            setGraphic(row);
            setText(null);
        }
    }
}