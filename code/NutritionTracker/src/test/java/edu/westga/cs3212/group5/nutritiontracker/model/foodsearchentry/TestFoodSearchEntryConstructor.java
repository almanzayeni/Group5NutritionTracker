package edu.westga.cs3212.group5.nutritiontracker.model.foodsearchentry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodSearchEntry;

public class TestFoodSearchEntryConstructor {

    @Test
    void testNoArgConstructorCreatesObject() {
        FoodSearchEntry entry = new FoodSearchEntry();
        assertNotNull(entry);
    }

    @Test
    void testDefaultValuesAreZeroOrNull() {
        FoodSearchEntry entry = new FoodSearchEntry();
        assertNull(entry.getDescription());
        assertEquals(0.0, entry.getCalories(), 0.001);
        assertEquals(0.0, entry.getProtein(), 0.001);
        assertEquals(0.0, entry.getFat(), 0.001);
        assertEquals(0.0, entry.getSugar(), 0.001);
        assertEquals(0.0, entry.getCarbohydrates(), 0.001);
        assertEquals(0.0, entry.getSodium(), 0.001);
    }

    @Test
    void testToStringContainsDescriptionAndCalories() {
        FoodSearchEntry entry = new FoodSearchEntry();
        entry.setDescription("Apple");
        entry.setCalories(95.0);
        String result = entry.toString();
        assertTrue(result.contains("Apple"));
        assertTrue(result.contains("95"));
    }
}
