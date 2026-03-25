package edu.westga.cs3212.group5.nutritiontracker.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class FoodLog.
 * 
 * @author Justin Smith
 * @version spring 2026
 */
public class FoodLog {
	private LocalDate date;
	private List<FoodItem> breakfast;
	private List<FoodItem> lunch;
	private List<FoodItem> dinner;
	private List<FoodItem> snacks;

	/**
	 * Instantiates a new food log to model the food consumed on a given day. The
	 * date is set to the current date and the meals are initialized to empty lists.
	 */
	public FoodLog() {
		this.date = LocalDate.now();
		this.breakfast = new ArrayList<FoodItem>();
		this.lunch = new ArrayList<FoodItem>();
		this.dinner = new ArrayList<FoodItem>();
		this.snacks = new ArrayList<FoodItem>();
	}

	/**
	 * Instantiates a new food log to model the food consumed on a given day. The
	 * date is set to the provided date and the meals are initialized to empty
	 * lists.
	 *
	 * @param date the date
	 */
	public FoodLog(LocalDate date) {
		this.date = date;
		this.breakfast = new ArrayList<FoodItem>();
		this.lunch = new ArrayList<FoodItem>();
		this.dinner = new ArrayList<FoodItem>();
		this.snacks = new ArrayList<FoodItem>();
	}

	/**
	 * Instantiates a new food log to model the food consumed on a given day. The
	 * date is set to the provided date and the meals are set to the provided lists.
	 *
	 * @param date      the date
	 * @param breakfast the breakfast
	 * @param lunch     the lunch
	 * @param dinner    the dinner
	 * @param snacks    the snacks
	 */
	public FoodLog(LocalDate date, List<FoodItem> breakfast, List<FoodItem> lunch, List<FoodItem> dinner,
			List<FoodItem> snacks) {
		this.date = date;
		this.breakfast = breakfast;
		this.lunch = lunch;
		this.dinner = dinner;
		this.snacks = snacks;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * Gets the breakfast list.
	 *
	 * @return the breakfast list
	 */
	public List<FoodItem> getBreakfast() {
		return this.breakfast;
	}

	/**
	 * Gets the lunch list.
	 *
	 * @return the lunch list
	 */
	public List<FoodItem> getLunch() {
		return this.lunch;
	}

	/**
	 * Gets the dinner list.
	 *
	 * @return the dinner list
	 */
	public List<FoodItem> getDinner() {
		return this.dinner;
	}

	/**
	 * Gets the snacks list.
	 *
	 * @return the snacks list
	 */
	public List<FoodItem> getSnacks() {
		return this.snacks;
	}
}
