package edu.westga.cs3212.group5.nutritiontracker.server;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class CreateFoodRequestHandler.
 *
 * @author vfilpo :)
 * @version spring 2026
 */
public class CreateFoodRequestHandler {

    /**
     * Creates the food request.
     *
     * @precondition description != null && !description.isBlank() &&
     *               quantityCategory != null && !quantityCategory.isBlank() &&
     *               portionSize > 0 && calories >= 0 && protein >= 0 &&
     *               fat >= 0 && sugar >= 0 && carbs >= 0 && sodium >= 0
     *
     * @param description      the food description
     * @param quantityCategory the quantity category ("SERVING", "WEIGHT", or "QUANTITY")
     * @param portionSize      the portion size
     * @param calories         the calories
     * @param protein          the protein in grams
     * @param fat              the fat in grams
     * @param sugar            the sugar in grams
     * @param carbs            the carbs in grams
     * @param sodium           the sodium in milligrams
     * @return the create food request as a JSON string
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static String createFoodRequest(String description, String quantityCategory,
            double portionSize, double calories, double protein,
            double fat, double sugar, double carbs, double sodium) {

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (quantityCategory == null || quantityCategory.isBlank()) {
            throw new IllegalArgumentException("Quantity category cannot be null or blank");
        }
        if (portionSize <= 0) {
            throw new IllegalArgumentException("Portion size must be greater than 0");
        }
        if (calories < 0 || protein < 0 || fat < 0 || sugar < 0 || carbs < 0 || sodium < 0) {
            throw new IllegalArgumentException("Nutritional values cannot be negative");
        }

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(ServerConstants.KEY_REQUEST_TYPE, ServerConstants.CREATE_FOOD_REQUEST_TYPE);
        requestMap.put(ServerConstants.KEY_FOOD_DESCRIPTION, description);
        requestMap.put(ServerConstants.KEY_FOOD_QUANTITY_CATEGORY, quantityCategory);
        requestMap.put(ServerConstants.KEY_FOOD_PORTION_SIZE, portionSize);
        requestMap.put(ServerConstants.KEY_FOOD_CALORIES, calories);
        requestMap.put(ServerConstants.KEY_FOOD_PROTEIN, protein);
        requestMap.put(ServerConstants.KEY_FOOD_FAT, fat);
        requestMap.put(ServerConstants.KEY_FOOD_SUGAR, sugar);
        requestMap.put(ServerConstants.KEY_FOOD_CARBS, carbs);
        requestMap.put(ServerConstants.KEY_FOOD_SODIUM, sodium);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create food request");
        }
    }

    /**
     * Handle create food request.
     *
     * @precondition request != null && !request.isBlank()
     *
     * @param request the create food request JSON string
     * @throws IllegalArgumentException if request is null or blank
     * @throws RuntimeException         if food creation fails or response is invalid
     */
    public static void handleCreateFoodRequest(String request) {
        if (request == null || request.isBlank()) {
            throw new IllegalArgumentException("Request cannot be null or blank");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            String response = ServerClient.send(request);
            if (response == null || response.isBlank()) {
                throw new RuntimeException("Received empty response from server");
            }

            var root = mapper.readTree(response);
            if (root.has(ServerConstants.KEY_FAILURE_MESSAGE)) {
                String failureMessage = root.get(ServerConstants.KEY_FAILURE_MESSAGE).asText();
                throw new RuntimeException("Food creation failed: " + failureMessage);
            }

            System.out.println("Food item created successfully: " + root);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to handle create food request", e);
        }
    }
}