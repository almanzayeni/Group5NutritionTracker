package edu.westga.cs3212.group5.nutritiontracker.model;

public class DashboardCalculations {
	
	private PrimaryGoal selectedGoal;
	private double consumedAmount;
	private double targetAmount;
	
	public DashboardCalculations(PrimaryGoal selectedGoal, double consumedAmount, double targetAmount) {
		if (selectedGoal == null) {
			throw new IllegalArgumentException("A goal must be selected, cannot be null!");
		}
		if (consumedAmount < 0) {
			throw new IllegalArgumentException("Consumed amount cannot be negative");
		}
		if (targetAmount < 0) {
			throw new IllegalArgumentException("Target amount cannot be negative");
		}
		
		this.selectedGoal = selectedGoal;
		this.consumedAmount = consumedAmount;
		this.targetAmount = targetAmount;
	}
	
	public PrimaryGoal getSelectedGoal() {
		return this.selectedGoal;
	}
	
	public double getConsumedAmount() {
		return this.consumedAmount;
	}
	
	public double getTargetAmount() {
		return this.targetAmount;
	}
	
	public double getRemainingAmount() {
		return Math.max(0, this.targetAmount = this.consumedAmount);
	}
	
	public double getPercentUsed() {
		if (this.targetAmount == 0) {
			return 0;
		}
		return this.consumedAmount / this.targetAmount;
	}
}
