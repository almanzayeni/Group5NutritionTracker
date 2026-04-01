package edu.westga.cs3212.group5.nutritiontracker.server;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.User;

public class CreateAccountRequestHandler {
	public static String createCreateAccountRequest(String username, String password, String name,
			DietGoals dietGoals) {
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be null or blank");
		}
		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be null or blank");
		}
		if (dietGoals == null) {
			throw new IllegalArgumentException("Diet goals cannot be null");
		}

		Map<String, String> requestMap = new HashMap<>();
		requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.CREATE_ACCOUNT_REQUEST_TYPE);

		User newUser = new User(username, password, name, dietGoals, new FoodLog());

		ObjectMapper mapper = JsonMapperFactory.create();

		try {
			String userJson = mapper.writeValueAsString(newUser);
			System.out.println("User JSON for create account request: " + userJson);
			String requestTypeJson = mapper.writeValueAsString(requestMap);
			System.out.println("Request type JSON for create account request: " + requestTypeJson);
			requestTypeJson = requestTypeJson.substring(0, requestTypeJson.length() - 1);
			return requestTypeJson + ", \"" + ServerConstants.KEY_USER + "\": " + userJson + "}";
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create create account request");
		}

	}

	public static User handleCreateAccountRequest(String request) {
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
				throw new RuntimeException("Create Account failed: " + failureMessage);
			}

			if (!root.has(ServerConstants.KEY_USER)) {
				throw new RuntimeException("Create Account failed: User data is null");
			}

			User user = mapper.treeToValue(root.get(ServerConstants.KEY_USER), User.class);
			System.out.println("Create Account successful for user: " + user.getName());
			return user;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to handle create account request", e);
		}
	}
}
