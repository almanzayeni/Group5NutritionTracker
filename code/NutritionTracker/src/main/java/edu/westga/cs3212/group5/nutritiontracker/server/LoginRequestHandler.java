package edu.westga.cs3212.group5.nutritiontracker.server;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.User;

/**
 * The Class LoginRequestHandler.
 * 
 * @author Justin Smith
 * @version spring 2026
 */
public class LoginRequestHandler {
	
	/**
	 * Creates the login request.
	 * 
	 * @precondition username != null && !username.isBlank() &&
	 * 				 password != null && !password.isBlank()
	 *
	 * @param username the username
	 * @param password the password
	 * @return the login request as a JSON string
	 * @throws IllegalArgumentException if username or password is null or blank
	 */
	public static String createLoginRequest(String username, String password) {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be null or blank");
		}
		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be null or blank");
		}

		Map<String, String> requestMap = new HashMap<>();
		requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.AUTHENTICATE_LOGIN_REQUEST_TYPE);
		requestMap.put(ServerConstants.KEY_USERNAME, username);
		requestMap.put(ServerConstants.KEY_PASSWORD, password);

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create login request");
		}
	}

	/**
	 * Handle login request.
	 * 
	 * @precondition request != null && !request.isBlank()
	 *
	 * @param request the login request JSON String
	 * @return the user object if login is successful
	 * @throws IllegalArgumentException if request is null or blank
	 * @throws RuntimeException if login fails or response is invalid
	 */
	public static User handleLoginRequest(String request) {
		if (request == null || request.isBlank()) {
			throw new IllegalArgumentException("Request cannot be null or blank");
		}

		ObjectMapper mapper = JsonMapperFactory.create();

		try {
			String response = ServerClient.send(request);
			if (response == null || response.isBlank()) {
				throw new RuntimeException("Received empty response from server");
			}

			var root = mapper.readTree(response);

			if (root.has(ServerConstants.KEY_FAILURE_MESSAGE)) {
				String failureMessage = root.get(ServerConstants.KEY_FAILURE_MESSAGE).asText();
				throw new RuntimeException("Login failed: " + failureMessage);
			}

			if (!root.has(ServerConstants.KEY_USER)) {
				throw new RuntimeException("Login failed: User data is null");
			}

			User user = mapper.treeToValue(root.get(ServerConstants.KEY_USER), User.class);
			System.out.println("Login successful for user: " + user.getName());
			return user;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to handle login request", e);
		}
	}
}
