package edu.westga.cs3212.group5.nutritiontracker.viewmodel.foodsearchviewmodel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodSearchEntry;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.FoodSearchViewModel;

public class TestFoodSearchViewModel {

    private FoodSearchViewModel vm;

    @BeforeEach
    void setUp() {
        this.vm = new FoodSearchViewModel();
    }
    
    @Test
    void testConstructorLoadsEntriesFromJson() {
        FoodSearchViewModel vm = new FoodSearchViewModel();
        assertFalse(vm.getSearchResults().isEmpty());
    }

    @Test
    void testConstructorWithBadPathReturnsEmptyResults() {
        FoodSearchViewModel vm = new FoodSearchViewModel("/nonexistent/path.json");
        assertTrue(vm.getSearchResults().isEmpty());
    }

    @Test
    void testInitialSearchTextIsEmpty() {
        FoodSearchViewModel vm = new FoodSearchViewModel();
        String text = vm.searchTextProperty().get();
        assertTrue(text == null || text.isEmpty());
    }

    @Test
    void testInitialSelectedEntryIsNull() {
        FoodSearchViewModel vm = new FoodSearchViewModel();
        assertNull(vm.getSelectedEntry());
    }

    @Test
    void testBlankSearchReturnsAllResults() {
        this.vm.searchTextProperty().set("");
        assertFalse(this.vm.getSearchResults().isEmpty());
    }

    @Test
    void testNullSearchReturnsAllResults() {
        this.vm.searchTextProperty().set(null);
        assertFalse(this.vm.getSearchResults().isEmpty());
    }

    @Test
    void testSearchMatchesDescription() {
        this.vm.searchTextProperty().set("apple");
        assertFalse(this.vm.getSearchResults().isEmpty());
        assertTrue(this.vm.getSearchResults().stream()
                .allMatch(e -> e.getDescription().toLowerCase().contains("apple")));
    }

    @Test
    void testSearchIsCaseInsensitive() {
        this.vm.searchTextProperty().set("APPLE");
        assertFalse(this.vm.getSearchResults().isEmpty());
        assertTrue(this.vm.getSearchResults().stream()
                .allMatch(e -> e.getDescription().toLowerCase().contains("apple")));
    }

    @Test
    void testSearchWithNoMatchReturnsEmpty() {
        this.vm.searchTextProperty().set("zzznomatchzzz");
        assertTrue(this.vm.getSearchResults().isEmpty());
    }

    @Test
    void testSearchUpdatesResultsWhenTextChanges() {
        this.vm.searchTextProperty().set("");
        int allCount = this.vm.getSearchResults().size();
        this.vm.searchTextProperty().set("chicken");
        int filteredCount = this.vm.getSearchResults().size();
        assertTrue(filteredCount < allCount);
    }
    
    @Test
    void testSetSelectedEntryUpdatesGetSelectedEntry() {
        FoodSearchEntry entry = new FoodSearchEntry();
        entry.setDescription("Apple");
        entry.setCalories(95.0);
        this.vm.setSelectedEntry(entry);
        assertEquals(entry, this.vm.getSelectedEntry());
    }

    @Test
    void testSetSelectedEntryNullClearsSelection() {
        FoodSearchEntry entry = new FoodSearchEntry();
        entry.setDescription("Apple");
        this.vm.setSelectedEntry(entry);
        this.vm.setSelectedEntry(null);
        assertNull(this.vm.getSelectedEntry());
    }

    @Test
    void testSelectedEntryPropertyReflectsSetValue() {
        FoodSearchEntry entry = new FoodSearchEntry();
        entry.setDescription("Banana");
        this.vm.setSelectedEntry(entry);
        assertEquals(entry, this.vm.selectedEntryProperty().get());
    }

    @Test
    void testSelectionIsIndependentOfSearchResults() {
        FoodSearchEntry entry = new FoodSearchEntry();
        entry.setDescription("Custom Food");
        this.vm.setSelectedEntry(entry);
        this.vm.searchTextProperty().set("apple");
        assertEquals(entry, this.vm.getSelectedEntry());
    }
    
}
