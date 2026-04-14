package edu.westga.cs3212.group5.nutritiontracker.model.sortoption;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.SortOption;

/**
 * Tests for SortOption enum.
 *
 * @author Group 5
 * @version Spring 2026
 */
public class TestSortOption {

    @Test
    void testNameAscToString() {
        assertEquals("Name (A-Z)", SortOption.NAME_ASC.toString());
    }

    @Test
    void testNameDescToString() {
        assertEquals("Name (Z-A)", SortOption.NAME_DESC.toString());
    }

    @Test
    void testCaloriesAscToString() {
        assertEquals("Calories (Low to High)", SortOption.CALORIES_ASC.toString());
    }

    @Test
    void testCaloriesDescToString() {
        assertEquals("Calories (High to Low)", SortOption.CALORIES_DESC.toString());
    }

    @Test
    void testAllValuesPresent() {
        assertEquals(4, SortOption.values().length);
    }
}