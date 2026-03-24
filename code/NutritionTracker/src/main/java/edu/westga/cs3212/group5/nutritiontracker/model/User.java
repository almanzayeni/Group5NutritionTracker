package edu.westga.cs3212.group5.nutritiontracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	private String username;
	private String password;
	private String name;
	private DietGoals dietGoals;
	private FoodLog currentFoodLog;
	
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

	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getName() {
		return this.name;
	}
	
	public DietGoals getDietGoals() {
		return this.dietGoals;
	}
	
	public FoodLog getCurrentFoodLog() {
		return this.currentFoodLog;
	}
}
