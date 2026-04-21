package edu.westga.cs3212.group5.nutritiontracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class User.
 * 
 * @author Justin Smith
 * @version spring 2026
 */
public class User {
	private String username;
	private String password;
	private String name;
	private DietGoals dietGoals;
	private FoodLog currentFoodLog;

	/**
	 * Instantiates a new user.
	 * 
	 * @precondition username != null && !username.isBlank() &&
	 * 				 password != null && !password.isBlank() &&
	 * 				 name != null && !name.isBlank() &&
	 * 				 dietGoals != null &&
	 * 				 currentFoodLog != null
	 *
	 * @param username the username
	 * @param password the password
	 * @param name the name
	 * @param dietGoals the diet goals
	 * @param currentFoodLog the current food log
	 */
	@JsonCreator
	public User(@JsonProperty("username") String username, @JsonProperty("password") String password,
			@JsonProperty("name") String name, @JsonProperty("dietGoals") DietGoals dietGoals,
			@JsonProperty("currentFoodLog") FoodLog currentFoodLog) {

		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be null or blank");
		}
		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be null or blank");
		}
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be null or blank");
		}
		if (dietGoals == null) {
			throw new IllegalArgumentException("Diet goals cannot be null");
		}
		if (currentFoodLog == null) {
			throw new IllegalArgumentException("Current food log cannot be null");
		}

		this.username = username;
		this.password = password;
		this.name = name;
		this.dietGoals = dietGoals;
		this.currentFoodLog = currentFoodLog;
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the diet goals.
	 *
	 * @return the diet goals
	 */
	public DietGoals getDietGoals() {
		return this.dietGoals;
	}

	/**
	 * Gets the current food log.
	 *
	 * @return the current food log
	 */
	public FoodLog getCurrentFoodLog() {
		return this.currentFoodLog;
	}

	/**
	 * Sets the current food log.
	 *
	 * @param log the new current food log
	 * @throws IllegalArgumentException if log is null
	 */
	public void setCurrentFoodLog(FoodLog log) {
		if (log == null) {
			throw new IllegalArgumentException("Current food log cannot be null");
		}
		this.currentFoodLog = log;
	}
}
