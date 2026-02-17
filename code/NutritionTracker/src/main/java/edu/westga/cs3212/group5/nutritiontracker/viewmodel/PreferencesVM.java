package edu.westga.cs3212.group5.nutritiontracker.viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import edu.westga.cs3212.group5.nutritiontracker.model.UserPreferences;
import java.util.List;

public class PreferencesVM {

    private StringProperty calorie = new SimpleStringProperty("");
    private StringProperty protein = new SimpleStringProperty("");
    private StringProperty fat = new SimpleStringProperty("");
    private StringProperty sugar = new SimpleStringProperty("");
    private StringProperty sodium = new SimpleStringProperty("");
    private StringProperty carbs = new SimpleStringProperty("");
    private StringProperty warning = new SimpleStringProperty("");

    public StringProperty calorieProperty() { 
    	return this.calorie; 
    }
    
    public StringProperty proteinProperty() { 
    	return this.protein; 
    }
    
    public StringProperty fatProperty() { 
    	return this.fat; 
    }
    
    public StringProperty sugarProperty() { 
    	return this.sugar; 
    }
    
    public StringProperty sodiumProperty() { 
    	return this.sodium; 
    }
    
    public StringProperty carbsProperty() { 
    	return this.carbs; 
    }
    
    public StringProperty warningProperty() { 
    	return this.warning; 
    }

    public UserPreferences createPreferences(String primaryGoal, String otherGoalsText) {
        try {
            UserPreferences prefs = new UserPreferences(
                primaryGoal,
                Double.parseDouble(this.calorie.get()),
                Double.parseDouble(this.protein.get()),
                Double.parseDouble(this.fat.get()),
                Double.parseDouble(this.sugar.get()),
                Double.parseDouble(this.sodium.get()),
                Double.parseDouble(this.carbs.get()),
                List.of(otherGoalsText.split(","))
            );

            this.warning.set("");
            return prefs;

        } catch (Exception e) {
            this.warning.set("Invalid input. Please enter valid numbers.");
            return null;
        }
    }
}