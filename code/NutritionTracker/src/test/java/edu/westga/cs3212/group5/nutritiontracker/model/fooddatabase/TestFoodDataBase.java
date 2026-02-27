package edu.westga.cs3212.group5.nutritiontracker.model.fooddatabase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodDatabase;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;

class TestFoodDataBase {

    private FoodDatabase db;

    @BeforeEach
    void setUp() {
        this.db = FoodDatabase.getInstance();
    }

    @Test
    void testGetInstanceReturnsSameObject() {
        assertSame(FoodDatabase.getInstance(), FoodDatabase.getInstance());
    }

    @Test
    void testAddFoodIncreasesSize() {
        int before = this.db.getAllFoods().size();
        this.db.addFood(new BaseFood("UniqueFoodXYZ", QuantityCategory.QUANTITY,
                1.0, 50.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        assertEquals(before + 1, this.db.getAllFoods().size());
    }

    @Test
    void testAddFoodNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> this.db.addFood(null));
    }

    @Test
    void testGetAllFoodsIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () ->
                this.db.getAllFoods().add(
                        new BaseFood("X", QuantityCategory.QUANTITY, 1.0, 50.0,
                                0.0, 0.0, 0.0, 0.0, 0.0)));
    }

    @Test
    void testSearchNullReturnsAll() {
        assertEquals(this.db.getAllFoods().size(), this.db.search(null).size());
    }

    @Test
    void testSearchBlankReturnsAll() {
        assertEquals(this.db.getAllFoods().size(), this.db.search("   ").size());
    }

    @Test
    void testSearchKeywordIsCaseInsensitiveAndFilters() {
        List<FoodItem> results = this.db.search("apple");
        assertFalse(results.isEmpty());
        assertTrue(results.stream()
                .allMatch(f -> f.getDescription().toLowerCase().contains("apple")));
    }

    @Test
    void testSearchNoMatchReturnsEmpty() {
        assertTrue(this.db.search("zzznomatchzzz").isEmpty());
    }

    // ── search(String, SortOption) ────────────────────────────────────────────

    @Test
    void testSortNameAscIsAlphabetical() {
        List<FoodItem> r = this.db.search(null, FoodDatabase.SortOption.NAME_ASC);
        for (int i = 0; i < r.size() - 1; i++) {
            assertTrue(r.get(i).getDescription()
                    .compareToIgnoreCase(r.get(i + 1).getDescription()) <= 0);
        }
    }

    @Test
    void testSortNameDescIsReverseAlphabetical() {
        List<FoodItem> r = this.db.search(null, FoodDatabase.SortOption.NAME_DESC);
        for (int i = 0; i < r.size() - 1; i++) {
            assertTrue(r.get(i).getDescription()
                    .compareToIgnoreCase(r.get(i + 1).getDescription()) >= 0);
        }
    }

    @Test
    void testSortCaloriesAscIsOrdered() {
        List<FoodItem> r = this.db.search(null, FoodDatabase.SortOption.CALORIES_ASC);
        for (int i = 0; i < r.size() - 1; i++) {
            assertTrue(r.get(i).getCalories() <= r.get(i + 1).getCalories());
        }
    }

    @Test
    void testSortCaloriesDescIsOrdered() {
        List<FoodItem> r = this.db.search(null, FoodDatabase.SortOption.CALORIES_DESC);
        for (int i = 0; i < r.size() - 1; i++) {
            assertTrue(r.get(i).getCalories() >= r.get(i + 1).getCalories());
        }
    }

    @Test
    void testSortNullReturnsResultsUnsorted() {
        assertFalse(this.db.search("apple", null).isEmpty());
    }

}
