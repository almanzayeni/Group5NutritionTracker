package edu.westga.cs3212.group5.nutritiontracker.viewmodel.dietgoalsviewmodel;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.DietGoalsViewModel;
import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;

/**
 * Unit tests for PreferencesVM.
 *
 * @author Yeni Almanza
 * @version spring 2026
 */
class TestDietGoalsViewModel {

    private DietGoalsViewModel viewModel;

    @BeforeEach
    void setUp() {
        this.viewModel = new DietGoalsViewModel();
    }

    @Test
    void testInitialPropertyValuesAreZero() {
        assertEquals(0.0, this.viewModel.calorieProperty().get());
        assertEquals(0.0, this.viewModel.proteinProperty().get());
        assertEquals(0.0, this.viewModel.fatProperty().get());
        assertEquals(0.0, this.viewModel.sugarProperty().get());
        assertEquals(0.0, this.viewModel.sodiumProperty().get());
        assertEquals(0.0, this.viewModel.carbsProperty().get());
    }

    @Test
    void testInitialWarningIsEmpty() {
        assertTrue(this.viewModel.warningProperty().get().isEmpty());
    }

    @Test
    void testCreatePreferencesReturnsValidObject() {
        this.viewModel.calorieProperty().set(2000);
        this.viewModel.proteinProperty().set(150);
        this.viewModel.fatProperty().set(65);
        this.viewModel.sugarProperty().set(50);
        this.viewModel.sodiumProperty().set(2300);
        this.viewModel.carbsProperty().set(250);

        DietGoals prefs = this.viewModel.createDietGoals(
                PrimaryGoal.CALORIE, "Drink water, Sleep well");

        assertNotNull(prefs);
        assertEquals(PrimaryGoal.CALORIE, prefs.getPrimaryGoal());
        assertEquals(2000, prefs.getCalorieGoal());
        assertEquals(150,  prefs.getProteinGoal());
        assertEquals(65,   prefs.getFatGoal());
        assertEquals(50,   prefs.getSugarGoal());
        assertEquals(2300, prefs.getSodiumGoal());
        assertEquals(250,  prefs.getCarbsGoal());
    }

    @Test
    void testCreatePreferencesOtherGoalsParsedCorrectly() {
        this.viewModel.calorieProperty().set(1800);

        DietGoals prefs = this.viewModel.createDietGoals(
                PrimaryGoal.OTHER, "Goal A, Goal B, Goal C");

        assertNotNull(prefs);
        List<String> others = prefs.getOtherGoals();
        assertEquals(3, others.size());
        assertTrue(others.contains("Goal A"));
        assertTrue(others.contains("Goal B"));
        assertTrue(others.contains("Goal C"));
    }

    @Test
    void testCreatePreferencesBlankOtherGoalsProducesEmptyList() {
        this.viewModel.calorieProperty().set(2200);

        DietGoals prefs = this.viewModel.createDietGoals(
                PrimaryGoal.PROTEIN, "   ");

        assertNotNull(prefs);
        assertTrue(prefs.getOtherGoals().isEmpty());
    }

    @Test
    void testCreatePreferencesNullOtherGoalsProducesEmptyList() {
        this.viewModel.calorieProperty().set(2200);

        DietGoals prefs = this.viewModel.createDietGoals(
                PrimaryGoal.PROTEIN, null);

        assertNotNull(prefs);
        assertTrue(prefs.getOtherGoals().isEmpty());
    }

    @Test
    void testCreatePreferencesClearsWarningOnSuccess() {
        this.viewModel.warningProperty().set("Old warning");
        this.viewModel.calorieProperty().set(2000);

        this.viewModel.createDietGoals(PrimaryGoal.CALORIE, "");

        assertTrue(this.viewModel.warningProperty().get().isEmpty());
    }

    @Test
    void testCreatePreferencesNullPrimaryGoalReturnsNull() {
        this.viewModel.calorieProperty().set(2000);

        DietGoals prefs = this.viewModel.createDietGoals(null, "");

        assertNull(prefs);
        assertFalse(this.viewModel.warningProperty().get().isEmpty());
    }

    @Test
    void testCreatePreferencesNegativeCalorieSetsWarning() {
        this.viewModel.calorieProperty().set(-1);

        DietGoals prefs = this.viewModel.createDietGoals(
                PrimaryGoal.CALORIE, "");

        assertNull(prefs);
        assertFalse(this.viewModel.warningProperty().get().isEmpty());
    }

    @Test
    void testPropertySetAndGet() {
        this.viewModel.calorieProperty().set(1500);
        this.viewModel.proteinProperty().set(120);
        this.viewModel.fatProperty().set(55);
        this.viewModel.sugarProperty().set(30);
        this.viewModel.sodiumProperty().set(1800);
        this.viewModel.carbsProperty().set(200);

        assertEquals(1500, this.viewModel.calorieProperty().get());
        assertEquals(120,  this.viewModel.proteinProperty().get());
        assertEquals(55,   this.viewModel.fatProperty().get());
        assertEquals(30,   this.viewModel.sugarProperty().get());
        assertEquals(1800, this.viewModel.sodiumProperty().get());
        assertEquals(200,  this.viewModel.carbsProperty().get());
    }
}
