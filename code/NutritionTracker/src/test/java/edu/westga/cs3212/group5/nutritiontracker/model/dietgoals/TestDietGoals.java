package edu.westga.cs3212.group5.nutritiontracker.model.dietgoals;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;

/**
 * Unit tests for UserPreferences.
 *
 * @author Yeni Almanza
 * @version spring 2026
 */
public class TestDietGoals {

    @Test
    void testConstructorValidAllGoals() {
        DietGoals prefs = new DietGoals(
                PrimaryGoal.CALORIE, 2000, 150, 65, 50, 2300, 250,
                List.of("Drink more water", "Sleep 8 hours"));

        assertEquals(PrimaryGoal.CALORIE, prefs.getPrimaryGoal());
        assertEquals(2000, prefs.getCalorieGoal());
        assertEquals(150,  prefs.getProteinGoal());
        assertEquals(65,   prefs.getFatGoal());
        assertEquals(50,   prefs.getSugarGoal());
        assertEquals(2300, prefs.getSodiumGoal());
        assertEquals(250,  prefs.getCarbsGoal());
        assertEquals(List.of("Drink more water", "Sleep 8 hours"), prefs.getOtherGoals());
    }

    @Test
    void testConstructorNullOtherGoalsTreatedAsEmpty() {
        DietGoals prefs = new DietGoals(
                PrimaryGoal.PROTEIN, 2200, 100, 70, 40, 1800, 300, null);

        assertNotNull(prefs.getOtherGoals());
        assertTrue(prefs.getOtherGoals().isEmpty());
    }

    @Test
    void testConstructorZeroNumericGoalsAllowed() {
        assertDoesNotThrow(() -> new DietGoals(
                PrimaryGoal.PROTEIN, 0, 0, 0, 0, 0, 0, null));
    }

    @Test
    void testOtherGoalsListIsUnmodifiable() {
        DietGoals prefs = new DietGoals(
                PrimaryGoal.CALORIE, 1800, 120, 60, 35, 2000, 220,
                List.of("Goal A"));

        assertThrows(UnsupportedOperationException.class,
                () -> prefs.getOtherGoals().add("Goal B"));
    }

    @Test
    void testConstructorNullPrimaryGoalThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new DietGoals(null, 2000, 150, 65, 50, 2300, 250, null));
    }

    @Test
    void testConstructorNegativeCalorieGoalThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new DietGoals(PrimaryGoal.CALORIE, -1, 150, 65, 50, 2300, 250, null));
    }

    @Test
    void testConstructorNegativeProteinGoalThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new DietGoals(PrimaryGoal.CALORIE, 2000, -1, 65, 50, 2300, 250, null));
    }

    @Test
    void testConstructorNegativeFatGoalThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new DietGoals(PrimaryGoal.CALORIE, 2000, 150, -1, 50, 2300, 250, null));
    }

    @Test
    void testConstructorNegativeSugarGoalThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new DietGoals(PrimaryGoal.CALORIE, 2000, 150, 65, -1, 2300, 250, null));
    }

    @Test
    void testConstructorNegativeSodiumGoalThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new DietGoals(PrimaryGoal.CALORIE, 2000, 150, 65, 50, -1, 250, null));
    }

    @Test
    void testConstructorNegativeCarbsGoalThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new DietGoals(PrimaryGoal.CALORIE, 2000, 150, 65, 50, 2300, -1, null));
    }

    @Test
    void testGetPrimaryGoalReturnsCorrectEnum() {
        DietGoals prefs = new DietGoals(
                PrimaryGoal.PROTEIN, 2500, 180, 80, 45, 2500, 300, null);
        assertEquals(PrimaryGoal.PROTEIN, prefs.getPrimaryGoal());
    }
}
