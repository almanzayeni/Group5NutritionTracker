package edu.westga.cs3212.group5.nutritiontracker.server;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.User;

public class EditDietGoalsHandler {
	
	/**
	 * Creates the edit diet goals request.
	 *
	 * @param username 			the username of the user whose diet goals are being edited
	 * @param dietGoals 		the diet goals to set for the user
	 * @return the string JSON request to send to the server
	 */
	public static String createEditDietGoalsRequest(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User cannot be null");
		}

		ObjectMapper mapper = JsonMapperFactory.create();

		try {
			JsonNode dietGoalsNode = mapper.valueToTree(user.getDietGoals());

			Map<String, Object> requestMap = new HashMap<>();
			requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.EDIT_DIET_GOALS_REQUEST_TYPE);
			requestMap.put(ServerConstants.KEY_USERNAME, user.getUsername());
			requestMap.put(ServerConstants.KEY_PASSWORD, user.getPassword());
			requestMap.put(ServerConstants.KEY_DIET_GOALS, dietGoalsNode);

			return mapper.writeValueAsString(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create add food request");
		}

	}

	/**
	 * Handle edit diet goals request.
	 *
	 * @param request 						the request as a JSON string
	 * @throws IllegalArgumentException 	if request is null or blank
	 * @throws RuntimeException         	if the server returns a failure or cannot be reached.
	 */
	public static void handleEditDietGoalsRequest(String request) {
		if (request == null || request.isBlank()) {
	        throw new IllegalArgumentException("Request cannot be null or blank");
	    }

	    ObjectMapper mapper = JsonMapperFactory.create();
	    try {
	        String response = ServerClient.send(request);

	        if (response == null || response.isBlank()) {
	            throw new RuntimeException("Received empty response from server");
	        }

	        JsonNode root = mapper.readTree(response);

	        if (root.has(ServerConstants.KEY_FAILURE_MESSAGE)) {
	            String failureMessage = root.get(ServerConstants.KEY_FAILURE_MESSAGE).asText();
	            throw new RuntimeException("Add food failed: " + failureMessage);
	        }

	        String status = root.get(ServerConstants.KEY_STATUS).asText();
	        if (!status.equals(ServerConstants.SUCCESS_STATUS)) {
	            throw new RuntimeException("Add food failed with status: " + status);
	        }

	    } catch (RuntimeException e) {
	        throw e;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to handle add food request", e);
	    }
	}
}
