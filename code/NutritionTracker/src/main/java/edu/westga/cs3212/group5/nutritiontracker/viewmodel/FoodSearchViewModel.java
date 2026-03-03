package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodSearchEntry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewModel for the standalone food search bar.
 * Reads food data from food_search_data.json and exposes filtered results.
 * Does NOT depend on FoodDatabase, BaseFood, CompositeFood, or any other
 * group member's classes.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class FoodSearchViewModel {

    private static final String JSON_FILE = "/edu/westga/cs3212/group5/nutritiontracker/food_search_data.json";

    private final StringProperty searchText = new SimpleStringProperty("");
    private final ObservableList<FoodSearchEntry> searchResults = FXCollections.observableArrayList();
    private final ObjectProperty<FoodSearchEntry> selectedEntry = new SimpleObjectProperty<>();

    private List<FoodSearchEntry> allEntries;

    /** Creates the ViewModel and loads all food entries from JSON. */
    public FoodSearchViewModel() {
        loadFromJson(JSON_FILE);
        // Automatically re-filter whenever search text changes
        searchText.addListener((obs, oldVal, newVal) -> filterResults(newVal));
        filterResults("");
    }

    /**
     * For testing only — accepts a custom resource path.
     *
     * @param resourcePath path to a JSON file on the classpath
     */
    public FoodSearchViewModel(String resourcePath) {
        loadFromJson(resourcePath);
        searchText.addListener((obs, oldVal, newVal) -> filterResults(newVal));
        filterResults("");
    }

    /** Bind this to the TextField's textProperty. */
    public StringProperty searchTextProperty() {
        return searchText;
    }

    /** Bind this to the ListView's items. */
    public ObservableList<FoodSearchEntry> getSearchResults() {
        return searchResults;
    }

    /** Bind or listen to this to know which row the user selected. */
    public ObjectProperty<FoodSearchEntry> selectedEntryProperty() {
        return selectedEntry;
    }

    /** Returns the currently selected entry, or null. */
    public FoodSearchEntry getSelectedEntry() {
        return selectedEntry.get();
    }

    /** Call this when the user clicks / selects a row in the ListView. */
    public void setSelectedEntry(FoodSearchEntry entry) {
        selectedEntry.set(entry);
    }

    private void loadFromJson(String path) {
        try (InputStream in = getClass().getResourceAsStream(path)) {
            if (in == null) {
                allEntries = List.of();
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            allEntries = mapper.readValue(in, new TypeReference<List<FoodSearchEntry>>() {});
        } catch (IOException ex) {
            allEntries = List.of();
            ex.printStackTrace();
        }
    }

    private void filterResults(String query) {
        if (query == null || query.isBlank()) {
            searchResults.setAll(allEntries);
            return;
        }
        String lower = query.toLowerCase();
        List<FoodSearchEntry> filtered = allEntries.stream()
                .filter(e -> e.getDescription().toLowerCase().contains(lower))
                .collect(Collectors.toList());
        searchResults.setAll(filtered);
    }
}
