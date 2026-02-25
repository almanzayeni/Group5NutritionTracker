package edu.westga.cs3212.group5.nutritiontracker.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodDatabase;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodDatabase.SortOption;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * Controller for the reusable FoodSearchPanel component.
 *
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class FoodSearchPanelController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TextField searchTextField;
    @FXML private ComboBox<SortOption> sortComboBox;
    @FXML private ListView<FoodItem> resultsListView;
    @FXML private Label statusLabel;

    private Consumer<FoodItem> onFoodSelected;

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
                    if (newVal != null && this.onFoodSelected != null) {
                        this.onFoodSelected.accept(newVal);
                    }
                });

        this.refreshResults();
    }

    /**
     * Registers a callback that fires whenever the user selects (highlights)
     * a food item in the results list.
     *
     * @param callback a {@link Consumer} that receives the selected FoodItem;
     *                 may be null to clear
     */
    public void setOnFoodSelected(Consumer<FoodItem> callback) {
        this.onFoodSelected = callback;
    }

    /**
     * Returns the currently highlighted food in the results list, or
     * {@code null} if nothing is selected.
     *
     * @return the selected FoodItem, or null
     */
    public FoodItem getSelectedFood() {
        return this.resultsListView.getSelectionModel().getSelectedItem();
    }

    /**
     * Clears the search text and resets the results to the full list.
     */
    public void reset() {
        this.searchTextField.clear();
        this.sortComboBox.setValue(SortOption.NAME_ASC);
        this.refreshResults();
    }

    /**
     * Exposes the status label text property so the host can bind or update it.
     *
     * @return the status label text property
     */
    public javafx.beans.property.StringProperty statusTextProperty() {
        return this.statusLabel.textProperty();
    }

    /**
     * Queries {@link FoodDatabase} and refreshes the ListView.
     */
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
            if (item instanceof edu.westga.cs3212.group5.nutritiontracker.model.BaseFood bf) {
                catText = bf.getQuantityCategory().toString();
            } else if (item instanceof edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood cf) {
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
