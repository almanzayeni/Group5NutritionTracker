package edu.westga.cs3212.group5.nutritiontracker.server.updatelogfoodrequesthandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerConstants;
import edu.westga.cs3212.group5.nutritiontracker.server.UpdateFoodLogRequestHandler;

/**
 * Tests for UpdateFoodLogRequestHandler.
 *
 * @author Yeni Almanza
 * @version Spring 2026
 */
public class TestUpdateFoodLogRequestHandler {

    private static final String VALID_USERNAME = "johndoe";
    private FoodLog validFoodLog;

    @BeforeEach
    void setUp() {
        this.validFoodLog = new FoodLog(
            LocalDate.of(2026, 3, 30),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    @Test
    void testCreateRequest_nullUsername_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(null, this.validFoodLog)
        );
    }

    @Test
    void testCreateRequest_blankUsername_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            UpdateFoodLogRequestHandler.createUpdateFoodLogRequest("   ", this.validFoodLog)
        );
    }

    @Test
    void testCreateRequest_nullFoodLog_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(VALID_USERNAME, null)
        );
    }

    @Test
    void testCreateRequest_validArgs_containsRequestType() {
        String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
            VALID_USERNAME, this.validFoodLog);
        assertTrue(request.contains(ServerConstants.UPDATE_FOODLOG_REQUEST_TYPE));
    }

    @Test
    void testCreateRequest_validArgs_containsUsername() {
        String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
            VALID_USERNAME, this.validFoodLog);
        assertTrue(request.contains(VALID_USERNAME));
    }

    @Test
    void testCreateRequest_validArgs_containsFoodLogKey() {
        String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
            VALID_USERNAME, this.validFoodLog);
        assertTrue(request.contains(ServerConstants.KEY_CURRENT_FOOD_LOG));
    }

    @Test
    void testCreateRequest_validArgs_containsDate() {
        String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
            VALID_USERNAME, this.validFoodLog);
        assertTrue(request.contains("2026-03-30"));
    }

    @Test
    void testHandleRequest_nullRequest_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(null)
        );
    }

    @Test
    void testHandleRequest_blankRequest_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest("   ")
        );
    }

    @Test
    void testHandleRequest_serverReturnsSuccess_noExceptionThrown() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"1\"}");

            assertDoesNotThrow(() ->
                UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(
                    "{\"request_type\":\"UPDATE_FOODLOG\"}")
            );
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testHandleRequest_serverReturnsFailureMessage_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"-1\",\"failure_message\":\"user not found\"}");

            RuntimeException ex = assertThrows(RuntimeException.class, () ->
                UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(
                    "{\"request_type\":\"UPDATE_FOODLOG\"}")
            );
            assertTrue(ex.getMessage().contains("user not found"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testHandleRequest_serverReturnsBadStatus_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"-1\"}");

            assertThrows(RuntimeException.class, () ->
                UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(
                    "{\"request_type\":\"UPDATE_FOODLOG\"}")
            );
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testHandleRequest_serverReturnsEmptyResponse_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("");

            assertThrows(RuntimeException.class, () ->
                UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(
                    "{\"request_type\":\"UPDATE_FOODLOG\"}")
            );
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testHandleRequest_serverReturnsNullResponse_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn(null);

            assertThrows(RuntimeException.class, () ->
                UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(
                    "{\"request_type\":\"UPDATE_FOODLOG\"}")
            );
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testHandleRequest_serverThrowsException_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenThrow(new Exception("connection refused"));

            assertThrows(RuntimeException.class, () ->
                UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(
                    "{\"request_type\":\"UPDATE_FOODLOG\"}")
            );
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testRoundTrip_createThenHandle_succeeds() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"1\"}");

            String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
                VALID_USERNAME, this.validFoodLog);

            assertDoesNotThrow(() ->
                UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(request)
            );
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
