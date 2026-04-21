package edu.westga.cs3212.group5.nutritiontracker.server;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;

/**
 * Handles UPDATE_FOODLOG requests to the server.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class UpdateFoodLogRequestHandler {

    /**
     * Creates the UPDATE_FOODLOG request JSON string to send to the server.
     *
     * @precondition username != null && !username.isBlank() && foodLog != null
     *
     * @param username the username of the user whose food log is being updated
     * @param foodLog  the updated food log
     * @return the request as a JSON string
     */
    public static String createUpdateFoodLogRequest(String username, FoodLog foodLog) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (foodLog == null) {
            throw new IllegalArgumentException("Food log cannot be null");
        }

        ObjectMapper mapper = JsonMapperFactory.create();
        try {
            JsonNode foodLogNode = mapper.valueToTree(foodLog);

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.UPDATE_FOODLOG_REQUEST_TYPE);
            requestMap.put(ServerConstants.KEY_USERNAME, username);
            requestMap.put(ServerConstants.KEY_FOOD_LOG, foodLogNode);

            return mapper.writeValueAsString(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create update food log request", e);
        }
    }

    /**
     * Sends the UPDATE_FOODLOG request to the server and handles the response.
     *
     * @precondition request != null && !request.isBlank()
     *
     * @param request the JSON request string
     */
    public static void handleUpdateFoodLogRequest(String request) {
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
                throw new RuntimeException("Update food log failed: " + failureMessage);
            }

            String status = root.get(ServerConstants.KEY_STATUS).asText();
            if (!status.equals(ServerConstants.SUCCESS_STATUS)) {
                throw new RuntimeException("Update food log failed with status: " + status);
            }

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to handle update food log request", e);
        }
    }
}
