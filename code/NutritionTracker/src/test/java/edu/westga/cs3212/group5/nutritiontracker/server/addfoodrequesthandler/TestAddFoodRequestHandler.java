package edu.westga.cs3212.group5.nutritiontracker.server.addfoodrequesthandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.server.AddFoodRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;

/**
 * Tests for AddFoodRequestHandler.
 *
 * @author (your name)
 * @version Spring 2026
 */
public class TestAddFoodRequestHandler {

    private static FoodItem makeFood() {
        return new BaseFood("oatmeal", QuantityCategory.SERVING, 1, 150, 5, 3, 1, 27, 0);
    }

    @Test
    public void testCreateAddFoodRequest_nullFood_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AddFoodRequestHandler.createAddFoodRequest(null);
        });
    }

    @Test
    public void testCreateAddFoodRequest_validFood_returnsJsonContainingRequestType() {
        String request = AddFoodRequestHandler.createAddFoodRequest(makeFood());
        assertTrue(request.contains(ServerConstants.ADD_FOOD_REQUEST_TYPE));
    }

    @Test
    public void testCreateAddFoodRequest_validFood_returnsJsonContainingFoodItem() {
        String request = AddFoodRequestHandler.createAddFoodRequest(makeFood());
        assertTrue(request.contains(ServerConstants.KEY_FOOD_ITEM));
    }

    @Test
    public void testCreateAddFoodRequest_validFood_returnsJsonContainingFoodDescription() {
        String request = AddFoodRequestHandler.createAddFoodRequest(makeFood());
        assertTrue(request.contains("oatmeal"));
    }

    @Test
    public void testHandleAddFoodRequest_nullRequest_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AddFoodRequestHandler.handleAddFoodRequest(null);
        });
    }

    @Test
    public void testHandleAddFoodRequest_blankRequest_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            AddFoodRequestHandler.handleAddFoodRequest("   ");
        });
    }

    @Test
    public void testHandleAddFoodRequest_serverReturnsSuccess_noExceptionThrown() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"1\"}");

            assertDoesNotThrow(() -> {
                AddFoodRequestHandler.handleAddFoodRequest("{\"request_type\":\"ADD_FOOD\"}");
            });
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testHandleAddFoodRequest_serverReturnsFailureMessage_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"-1\",\"failure_message\":\"food already exists\"}");

            RuntimeException ex = assertThrows(RuntimeException.class, () -> {
                AddFoodRequestHandler.handleAddFoodRequest("{\"request_type\":\"ADD_FOOD\"}");
            });
            assertTrue(ex.getMessage().contains("food already exists"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testHandleAddFoodRequest_serverReturnsBadStatus_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"-1\"}");

            assertThrows(RuntimeException.class, () -> {
                AddFoodRequestHandler.handleAddFoodRequest("{\"request_type\":\"ADD_FOOD\"}");
            });
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testHandleAddFoodRequest_serverReturnsEmptyResponse_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("");

            assertThrows(RuntimeException.class, () -> {
                AddFoodRequestHandler.handleAddFoodRequest("{\"request_type\":\"ADD_FOOD\"}");
            });
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testHandleAddFoodRequest_serverThrowsException_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenThrow(new Exception("connection refused"));

            assertThrows(RuntimeException.class, () -> {
                AddFoodRequestHandler.handleAddFoodRequest("{\"request_type\":\"ADD_FOOD\"}");
            });
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}