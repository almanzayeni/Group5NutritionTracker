package edu.westga.cs3212.group5.nutritiontracker.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodLog {
	private LocalDate date;
	private List<FoodItem> breakfast;
	private List<FoodItem> lunch;
	private List<FoodItem> dinner;
	private List<FoodItem> snacks;

	public FoodLog() {
		this.date = LocalDate.now();
		this.breakfast = new ArrayList<FoodItem>();
		this.lunch = new ArrayList<FoodItem>();
		this.dinner = new ArrayList<FoodItem>();
		this.snacks = new ArrayList<FoodItem>();
	}

	public FoodLog(LocalDate date) {
		this.date = date;
		this.breakfast = new ArrayList<FoodItem>();
		this.lunch = new ArrayList<FoodItem>();
		this.dinner = new ArrayList<FoodItem>();
		this.snacks = new ArrayList<FoodItem>();
	}

	public FoodLog(LocalDate date, List<FoodItem> breakfast, List<FoodItem> lunch, List<FoodItem> dinner,
			List<FoodItem> snacks) {
		this.date = date;
		this.breakfast = breakfast;
		this.lunch = lunch;
		this.dinner = dinner;
		this.snacks = snacks;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public List<FoodItem> getBreakfast() {
		return this.breakfast;
	}

	public List<FoodItem> getLunch() {
		return this.lunch;
	}

	public List<FoodItem> getDinner() {
		return this.dinner;
	}

	public List<FoodItem> getSnacks() {
		return this.snacks;
	}
}
