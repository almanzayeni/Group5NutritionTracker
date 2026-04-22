package edu.westga.cs3212.group5.nutritiontracker.model.dashboardcalculations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.DashboardCalculations;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;

public class TestDashboardCalculations {

	@Test
	void testConstructorNullGoalThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> new DashboardCalculations(null, 10, 20));
	}

	@Test
	void testConstructorNegativeConsumedAmountThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> new DashboardCalculations(PrimaryGoal.CALORIE, -1, 20));
	}

	@Test
	void testConstructorNegativeTargetAmountThrowsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> new DashboardCalculations(PrimaryGoal.CALORIE, 10, -1));
	}

	@Test
	void testGettersReturnConstructorValues() {
		DashboardCalculations calculations = new DashboardCalculations(PrimaryGoal.PROTEIN, 75, 120);

		assertEquals(PrimaryGoal.PROTEIN, calculations.getSelectedGoal());
		assertEquals(75, calculations.getConsumedAmount(), 0.001);
		assertEquals(120, calculations.getTargetAmount(), 0.001);
	}

	@Test
	void testGetRemainingAmountReturnsPositiveDifference() {
		DashboardCalculations calculations = new DashboardCalculations(PrimaryGoal.FAT, 20, 60);

		assertEquals(40, calculations.getRemainingAmount(), 0.001);
	}

	@Test
	void testGetRemainingAmountClampsAtZero() {
		DashboardCalculations calculations = new DashboardCalculations(PrimaryGoal.SODIUM, 2500, 2300);

		assertEquals(0, calculations.getRemainingAmount(), 0.001);
	}

	@Test
	void testGetPercentUsedReturnsZeroWhenTargetIsZero() {
		DashboardCalculations calculations = new DashboardCalculations(PrimaryGoal.OTHER, 10, 0);

		assertEquals(0, calculations.getPercentUsed(), 0.001);
	}

	@Test
	void testGetPercentUsedReturnsConsumedDividedByTarget() {
		DashboardCalculations calculations = new DashboardCalculations(PrimaryGoal.CALORIE, 500, 2000);

		assertEquals(0.25, calculations.getPercentUsed(), 0.001);
	}
}
