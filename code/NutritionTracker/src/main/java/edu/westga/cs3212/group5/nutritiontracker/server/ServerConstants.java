package edu.westga.cs3212.group5.nutritiontracker.server;

/**
 * The Class ServerConstants.
 * Holds all of the constant values used by the server.
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
	
	public static final String SUCCESS_STATUS = "1";
	public static final String BAD_MESSAGE_STATUS = "-1";
	public static final String UNSUPPORTED_OPERATION_STATUS = "-1";
	
	public static final String AUTHENTICATE_LOGIN_REQUEST_TYPE = "AUTH";
}
