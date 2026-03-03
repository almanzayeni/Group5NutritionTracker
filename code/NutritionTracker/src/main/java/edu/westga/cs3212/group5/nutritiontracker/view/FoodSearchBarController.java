package edu.westga.cs3212.group5.nutritiontracker.view;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodSearchEntry;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.FoodSearchViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for FoodSearchBar.fxml.
 * Uses a ContextMenu dropdown — no ListView needed in any host controller.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class FoodSearchBarController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private TextField searchTextField;

    private FoodSearchViewModel viewModel;
    private ContextMenu dropdown;

    @FXML
    void initialize() {
        assert searchTextField != null : "fx:id=\"searchTextField\" not injected";

        viewModel = new FoodSearchViewModel();
        dropdown = new ContextMenu();

        // Rebuild dropdown whenever search results change
        viewModel.getSearchResults().addListener(
            (javafx.collections.ListChangeListener<FoodSearchEntry>) change -> rebuildDropdown()
        );

        // Listen to text changes manually (no bidirectional binding)
        searchTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            // If the text matches the selected entry, user just picked something — don't re-filter
            if (viewModel.getSelectedEntry() != null
                    && newVal != null
                    && newVal.equals(viewModel.getSelectedEntry().getDescription())) {
                return;
            }

            // User is typing a new search — clear previous selection
            viewModel.setSelectedEntry(null);
            viewModel.searchTextProperty().set(newVal);

            if (newVal == null || newVal.isBlank()) {
                dropdown.hide();
            } else {
                rebuildDropdown();
                if (!dropdown.isShowing()) {
                    dropdown.show(searchTextField, javafx.geometry.Side.BOTTOM, 0, 0);
                }
            }
        });
    }

    /** Returns the currently selected entry, or null. */
    public FoodSearchEntry getSelectedEntry() {
        return viewModel.getSelectedEntry();
    }

    /** Bind to this to be notified when the user picks a result. */
    public ObjectProperty<FoodSearchEntry> selectedEntryProperty() {
        return viewModel.selectedEntryProperty();
    }

    /** Clears the search field and selection. */
    public void reset() {
        searchTextField.clear();
        viewModel.setSelectedEntry(null);
        dropdown.hide();
    }

    private void rebuildDropdown() {
        dropdown.getItems().clear();

        var results = viewModel.getSearchResults();

        if (results.isEmpty()) {
            MenuItem none = new MenuItem("No results available");
            none.setDisable(true);
            none.setStyle("-fx-text-fill: grey; -fx-font-style: italic;");
            dropdown.getItems().add(none);
        } else {
            for (FoodSearchEntry entry : results) {
                MenuItem item = new MenuItem(
                    entry.getDescription() + "  —  "
                    + String.format("%.0f", entry.getCalories()) + " cal"
                );
                item.setOnAction(e -> {
                    viewModel.setSelectedEntry(entry);
                    searchTextField.setText(entry.getDescription()); 
                    dropdown.hide();
                });
                dropdown.getItems().add(item);
            }
        }
    }
}