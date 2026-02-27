package edu.westga.cs3212.group5.nutritiontracker.view;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodDatabase;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodDatabase.SortOption;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;

import java.util.List;
import java.util.Optional;

/**
 * A modal dialog that lets the user search the {@link FoodDatabase} and select
 * a food item.  After the dialog closes, call {@link #getSelectedFood()} to
 * retrieve the user's choice (empty if the dialog was cancelled).
 *
 * <p>Usage example (inside a controller):
 * <pre>
 *   FoodSearchDialog dialog = new FoodSearchDialog(primaryStage);
 *   dialog.showAndWait();
 *   dialog.getSelectedFood().ifPresent(food -> {
 *       // populate fields with food's nutritional data
 *   });
 * </pre>
 *
 * @author Group 5
 * @version Spring 2026
 */
public class FoodSearchController {

    // ── State ─────────────────────────────────────────────────────────────────
    private FoodItem selectedFood = null;
    private final Stage dialogStage;

    // ── UI controls (kept as fields so listeners can reference them) ──────────
    private final TextField searchTextField;
    private final ComboBox<SortOption> sortComboBox;
    private final ListView<FoodItem> resultsListView;
    private final Label statusLabel;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Creates a new FoodSearchDialog owned by the given window.
     *
     * @param owner the parent window (may be null)
     */
    public FoodSearchController(Window owner) {
        this.dialogStage = new Stage();
        this.dialogStage.initModality(Modality.WINDOW_MODAL);
        if (owner != null) {
            this.dialogStage.initOwner(owner);
        }
        this.dialogStage.setTitle("Search Foods");
        this.dialogStage.setResizable(false);

        // ── Search bar row ────────────────────────────────────────────────────
        this.searchTextField = new TextField();
        this.searchTextField.setPromptText("Search... (e.g. chicken, apple)");
        HBox.setHgrow(this.searchTextField, Priority.ALWAYS);

        this.sortComboBox = new ComboBox<>(
                FXCollections.observableArrayList(SortOption.values()));
        this.sortComboBox.setValue(SortOption.NAME_ASC);
        this.sortComboBox.setPrefWidth(185);

        HBox searchRow = new HBox(8, this.searchTextField, this.sortComboBox);

        // ── Sort label row ────────────────────────────────────────────────────
        Label sortLabel = new Label("Sort:");
        sortLabel.setStyle("-fx-text-fill: #6e2316; -fx-font-weight: bold;");

        // ── Results list ──────────────────────────────────────────────────────
        this.resultsListView = new ListView<>();
        this.resultsListView.setPrefHeight(280);
        this.resultsListView.setCellFactory(lv -> new FoodCell());

        // ── Status / info label ───────────────────────────────────────────────
        this.statusLabel = new Label(" ");
        this.statusLabel.setStyle("-fx-text-fill: #6e2316;");
        this.statusLabel.setWrapText(true);

        // ── Buttons ───────────────────────────────────────────────────────────
        Button addButton = new Button("Add Selected");
        addButton.setStyle("-fx-background-color: #b8d5a3; -fx-font-weight: bold; -fx-font-size: 14;");
        addButton.setPrefWidth(150);

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #e8d5c4; -fx-font-weight: bold; -fx-font-size: 14;");
        cancelButton.setPrefWidth(100);

        HBox buttonRow = new HBox(10, addButton, cancelButton);

        // ── Layout ────────────────────────────────────────────────────────────
        Label titleLabel = new Label("Search Foods");
        titleLabel.setFont(new Font("Bookman Old Style Bold", 22));
        titleLabel.setStyle("-fx-text-fill: #1f5c33;");

        VBox root = new VBox(10,
                titleLabel,
                searchRow,
                this.resultsListView,
                this.statusLabel,
                buttonRow);
        root.setPadding(new Insets(16));
        root.setStyle("-fx-background-color: #F3EDE4;");

        Scene scene = new Scene(root, 440, 440);
        this.dialogStage.setScene(scene);

        // ── Listeners ─────────────────────────────────────────────────────────
        // Live search: refresh results whenever the user types
        this.searchTextField.textProperty().addListener((obs, oldVal, newVal) -> this.refreshResults());

        // Re-sort when sort option changes
        this.sortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> this.refreshResults());

        // Confirm selection
        addButton.setOnAction(event -> {
            FoodItem chosen = this.resultsListView.getSelectionModel().getSelectedItem();
            if (chosen == null) {
                this.statusLabel.setText("Please select a food from the list first.");
                return;
            }
            this.selectedFood = chosen;
            this.dialogStage.close();
        });

        // Double-click also confirms
        this.resultsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                FoodItem chosen = this.resultsListView.getSelectionModel().getSelectedItem();
                if (chosen != null) {
                    this.selectedFood = chosen;
                    this.dialogStage.close();
                }
            }
        });

        // Cancel
        cancelButton.setOnAction(event -> this.dialogStage.close());

        // Load initial results (all foods)
        this.refreshResults();
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Opens the dialog and blocks until it is closed.
     */
    public void showAndWait() {
        this.dialogStage.showAndWait();
    }

    /**
     * Returns the food selected by the user, or {@link Optional#empty()} if
     * the dialog was cancelled or no selection was made.
     *
     * @return an Optional containing the selected FoodItem
     */
    public Optional<FoodItem> getSelectedFood() {
        return Optional.ofNullable(this.selectedFood);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Queries the database and refreshes the results list view.
     */
    private void refreshResults() {
        String query = this.searchTextField.getText();
        SortOption sort = this.sortComboBox.getValue();

        List<FoodItem> results = FoodDatabase.getInstance().search(query, sort);
        this.resultsListView.setItems(FXCollections.observableArrayList(results));

        if (results.isEmpty()) {
            this.statusLabel.setText("No foods found matching \"" + query + "\".");
        } else {
            this.statusLabel.setText(results.size() + " result(s). Double-click or select then press Add.");
        }
    }

    // ── Inner cell class ──────────────────────────────────────────────────────

    /**
     * Custom ListCell that displays each FoodItem as:
     *   "Description  –  123 cal  |  CATEGORY"
     */
    private static class FoodCell extends ListCell<FoodItem> {

        @Override
        protected void updateItem(FoodItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            // Main label: name + calories
            Label nameLabel = new Label(item.getDescription());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");
            HBox.setHgrow(nameLabel, Priority.ALWAYS);

            Label calLabel = new Label(String.format("%.0f cal", item.getCalories()));
            calLabel.setStyle("-fx-text-fill: #555555; -fx-font-size: 12;");

            // Category tag (uses toString of enum for a tidy label)
            String catText = "—";
            if (item instanceof edu.westga.cs3212.group5.nutritiontracker.model.BaseFood bf) {
                catText = bf.getQuantityCategory().toString();
            } else if (item instanceof edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood cf) {
                catText = cf.getQuantityCategory().toString();
            }
            Label catLabel = new Label(catText);
            catLabel.setStyle(
                    "-fx-background-color: #b8d5a3; -fx-padding: 2 6 2 6; " +
                    "-fx-background-radius: 4; -fx-font-size: 11;");

            HBox row = new HBox(8, nameLabel, calLabel, catLabel);
            row.setPadding(new Insets(4, 2, 4, 2));
            setGraphic(row);
            setText(null);
        }
    }
}