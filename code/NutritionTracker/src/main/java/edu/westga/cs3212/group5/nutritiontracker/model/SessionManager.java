package edu.westga.cs3212.group5.nutritiontracker.model;

/**
 * A singleton class that holds current session information, such as the User currently logged in.
 * 
 * @author vfilpo :)
 * @version Spring 2026
 */
public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();
    private User currentUser;

    public static SessionManager getInstance() { return INSTANCE; }
    
    public void setCurrentUser(User user) {
    	this.currentUser = user;
    }
    public User getCurrentUser() {
    	return this.currentUser;
    }
}