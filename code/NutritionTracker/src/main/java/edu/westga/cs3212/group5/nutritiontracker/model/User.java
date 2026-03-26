package edu.westga.cs3212.group5.nutritiontracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a User of the system.
 * 
 * @author Justin Smith + vfilpo :)
 * @version Spring 2026
 */
public class User {
	private String username;
	private String password;
	private String name;
	private DietGoals dietGoals;
	private FoodLog currentFoodLog;
	
	/**
	 * Constructs a User with the specified credentials and dietary information.
	 *
	 * @param username       the user's unique identifier; must not be null or blank
	 * @param password       the user's password; must not be null or blank
	 * @param name           the user's display name; must not be null or blank
	 * @param dietGoals      the user's dietary goals; must not be null
	 * @param currentFoodLog the user's current food log; must not be null
	 * @throws IllegalArgumentException if any parameter is null, or if username, password, or name is blank
	 */
	@JsonCreator
    public User(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("name") String name,
            @JsonProperty("dietGoals") DietGoals dietGoals,
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
	 * Returns the user's unique identifier.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Returns the user's password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Returns the user's display name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the user's dietary goals.
	 *
	 * @return the {@link DietGoals}
	 */
	public DietGoals getDietGoals() {
		return this.dietGoals;
	}
	
	/**
	 * Returns the user's current food log.
	 *
	 * @return the {@link FoodLog}
	 */
	public FoodLog getCurrentFoodLog() {
		return this.currentFoodLog;
	}
}
