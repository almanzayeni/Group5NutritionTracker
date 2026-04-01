package edu.westga.cs3212.group5.nutritiontracker.server;

/**
 * The Class ServerConstants. Holds all of the constant values used by the
 * server.
 * 
 * @author Justin Smith
 * @version spring 2026
 */
public class ServerConstants {
	public static final String PROTOCOL = "tcp";
	public static final String IP_ADDRESS = "127.0.0.1";
	public static final String PORT = "5555";
	public static final String ADDRESS = PROTOCOL + "://" + IP_ADDRESS + ":" + PORT;

	public static final String EXIT_COMMAND = "exit";
	public static final String KEY_REQUEST_TYPE = "request_type";
	public static final String KEY_FAILURE_MESSAGE = "failure_message";
	public static final String KEY_STATUS = "status";
	public static final String KEY_USER = "user";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_NAME = "name";
	public static final String KEY_DIET_GOALS = "dietGoals";
	public static final String KEY_PRIMARY_GOAL = "primaryGoal";
	public static final String KEY_CALORIE_GOAL = "calorieGoal";
	public static final String KEY_PROTEIN_GOAL = "proteinGoal";
	public static final String KEY_FAT_GOAL = "fatGoal";
	public static final String KEY_SUGAR_GOAL = "sugarGoal";
	public static final String KEY_SODIUM_GOAL = "sodiumGoal";
	public static final String KEY_CARBS_GOAL = "carbsGoal";
	public static final String KEY_OTHER_GOALS = "otherGoals";
	public static final String KEY_QUERY = "query";
	public static final String KEY_SEARCH_RESULTS = "search_results";

	public static final String SUCCESS_STATUS = "1";
	public static final String BAD_MESSAGE_STATUS = "-1";
	public static final String UNSUPPORTED_OPERATION_STATUS = "-1";

	public static final String AUTHENTICATE_LOGIN_REQUEST_TYPE = "AUTH";
	public static final String CREATE_ACCOUNT_REQUEST_TYPE = "CREATE_ACCOUNT";
	public static final String SEARCH_REQUEST_TYPE = "SEARCH";
}
