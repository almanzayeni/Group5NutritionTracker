package edu.westga.cs3212.group5.nutritiontracker.model.foodsearchentry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodSearchEntry;

public class TestFoodSearchEntrySetters {

    private FoodSearchEntry entry;

    @BeforeEach
    void setUp() {
        this.entry = new FoodSearchEntry();
    }

    @Test
    void testSetAndGetDescription() {
        this.entry.setDescription("Banana");
        assertEquals("Banana", this.entry.getDescription());
    }

    @Test
    void testSetAndGetCalories() {
        this.entry.setCalories(105.0);
        assertEquals(105.0, this.entry.getCalories(), 0.001);
    }

    @Test
    void testSetAndGetProtein() {
        this.entry.setProtein(1.3);
        assertEquals(1.3, this.entry.getProtein(), 0.001);
    }

    @Test
    void testSetAndGetFat() {
        this.entry.setFat(0.4);
        assertEquals(0.4, this.entry.getFat(), 0.001);
    }

    @Test
    void testSetAndGetSugar() {
        this.entry.setSugar(14.0);
        assertEquals(14.0, this.entry.getSugar(), 0.001);
    }

    @Test
    void testSetAndGetCarbohydrates() {
        this.entry.setCarbohydrates(27.0);
        assertEquals(27.0, this.entry.getCarbohydrates(), 0.001);
    }

    @Test
    void testSetAndGetSodium() {
        this.entry.setSodium(1.0);
        assertEquals(1.0, this.entry.getSodium(), 0.001);
    }
}
