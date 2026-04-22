package edu.westga.cs3212.group5.nutritiontracker.server.updatelogfoodrequesthandler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.DietGoals;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodLog;
import edu.westga.cs3212.group5.nutritiontracker.model.JsonMapperFactory;
import edu.westga.cs3212.group5.nutritiontracker.model.PrimaryGoal;
import edu.westga.cs3212.group5.nutritiontracker.model.User;
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
    private static final String VALID_PASSWORD = "password123";
    private FoodLog validFoodLog;
    private User validUser;

    @BeforeEach
    void setUp() {
        this.validFoodLog = new FoodLog(
            LocalDate.of(2026, 3, 30),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
        );
        DietGoals goals = new DietGoals(
            PrimaryGoal.CALORIE,
            2000,
            150,
            70,
            50,
            2300,
            250,
            Collections.emptyList()
        );
        this.validUser = new User(
            VALID_USERNAME,
            VALID_PASSWORD,
            "John Doe",
            goals,
            this.validFoodLog
        );
    }

    @Test
    void testCreateRequest_nullUser_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
            UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(null)
        );
    }

    @Test
    void testCreateRequest_validArgs_containsRequestType() {
        String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
            this.validUser);
        assertTrue(request.contains(ServerConstants.UPDATE_FOODLOG_REQUEST_TYPE));
    }

    @Test
    void testCreateRequest_validArgs_containsUsername() {
        String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
            this.validUser);
        assertTrue(request.contains(VALID_USERNAME));
    }

    @Test
    void testCreateRequest_validArgs_containsPassword() {
        String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
            this.validUser);
        assertTrue(request.contains(VALID_PASSWORD));
    }

    @Test
    void testCreateRequest_validArgs_containsFoodLogKey() {
        String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
            this.validUser);
        assertTrue(request.contains(ServerConstants.KEY_FOOD_LOG));
    }

    @Test
    void testCreateRequest_validArgs_containsDate() {
        String request = UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(
            this.validUser);
        assertTrue(request.contains("2026-03-30"));
    }

    @Test
    void testCreateRequest_mapperFailure_wrapsInRuntimeException() throws Exception {
        ObjectMapper mapper = mock(ObjectMapper.class);
        JsonNode foodLogNode = mock(JsonNode.class);

        when(mapper.valueToTree(this.validFoodLog)).thenReturn(foodLogNode);
        when(mapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("boom") {
            private static final long serialVersionUID = 1L;
        });

        try (MockedStatic<JsonMapperFactory> mockFactory = mockStatic(JsonMapperFactory.class)) {
            mockFactory.when(JsonMapperFactory::create).thenReturn(mapper);

            RuntimeException ex = assertThrows(RuntimeException.class, () ->
                UpdateFoodLogRequestHandler.createUpdateFoodLogRequest(this.validUser)
            );

            assertEquals("Failed to create update food log request", ex.getMessage());
            assertTrue(ex.getCause() instanceof JsonProcessingException);
        }
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
                this.validUser);

            assertDoesNotThrow(() ->
                UpdateFoodLogRequestHandler.handleUpdateFoodLogRequest(request)
            );
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}
