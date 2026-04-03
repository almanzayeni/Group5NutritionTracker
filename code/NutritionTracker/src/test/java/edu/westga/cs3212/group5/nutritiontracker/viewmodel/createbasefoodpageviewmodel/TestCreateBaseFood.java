package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createbasefoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.server.AddFoodRequestHandler;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateBaseFoodPageViewModel;

/**
 * Tests for CreateBaseFoodPageViewModel.
 *
 * @author (your name)
 * @version Spring 2026
 */
public class TestCreateBaseFood {

    private CreateBaseFoodPageViewModel viewModel;

    @BeforeEach
    public void setUp() {
        this.viewModel = new CreateBaseFoodPageViewModel();
    }

    @Test
    public void testConstructor_descriptionPropertyInitializedEmpty() {
        assertTrue(this.viewModel.getDescriptionProperty().get() == null
                || this.viewModel.getDescriptionProperty().get().isEmpty());
    }

    @Test
    public void testConstructor_quantityCategoriesListContainsThreeItems() {
        assertEquals(3, this.viewModel.getQuantityCategoriesListProperty().size());
    }

    @Test
    public void testConstructor_quantityCategoriesListContainsQuantity() {
        assertTrue(this.viewModel.getQuantityCategoriesListProperty().contains(QuantityCategory.QUANTITY));
    }

    @Test
    public void testConstructor_quantityCategoriesListContainsWeight() {
        assertTrue(this.viewModel.getQuantityCategoriesListProperty().contains(QuantityCategory.WEIGHT));
    }

    @Test
    public void testConstructor_quantityCategoriesListContainsServing() {
        assertTrue(this.viewModel.getQuantityCategoriesListProperty().contains(QuantityCategory.SERVING));
    }

    @Test
    public void testConstructor_portionSizeDefaultsToOne() {
        assertEquals(1.0, this.viewModel.getPortionSize());
    }

    @Test
    public void testConstructor_caloriesPropertyInitializedToZero() {
        assertEquals(0.0, this.viewModel.getCaloriesProperty().get());
    }

    @Test
    public void testConstructor_proteinPropertyInitializedToZero() {
        assertEquals(0.0, this.viewModel.getProteinProperty().get());
    }

    @Test
    public void testConstructor_fatPropertyInitializedToZero() {
        assertEquals(0.0, this.viewModel.getFatProperty().get());
    }

    @Test
    public void testConstructor_sugarPropertyInitializedToZero() {
        assertEquals(0.0, this.viewModel.getSugarProperty().get());
    }

    @Test
    public void testConstructor_carbohydratesPropertyInitializedToZero() {
        assertEquals(0.0, this.viewModel.getCarbohydratesProperty().get());
    }

    @Test
    public void testConstructor_sodiumPropertyInitializedToZero() {
        assertEquals(0.0, this.viewModel.getSodiumProperty().get());
    }

    @Test
    public void testConstructor_selectedQuantityCategoryInitializedToNull() {
        assertNull(this.viewModel.getSelectedQuantityCategoryProperty().get());
    }

    @Test
    public void testCreateBaseFood_validFood_serverCalledSuccessfully() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"1\"}");

            this.viewModel.getDescriptionProperty().set("oatmeal");
            this.viewModel.getSelectedQuantityCategoryProperty().set(QuantityCategory.SERVING);
            this.viewModel.getCaloriesProperty().set(150);
            this.viewModel.getProteinProperty().set(5);
            this.viewModel.getFatProperty().set(3);
            this.viewModel.getSugarProperty().set(1);
            this.viewModel.getCarbohydratesProperty().set(27);
            this.viewModel.getSodiumProperty().set(0);

            assertDoesNotThrow(() -> this.viewModel.createBaseFood());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateBaseFood_serverReturnsFailure_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"-1\",\"failure_message\":\"food already exists\"}");

            this.viewModel.getDescriptionProperty().set("oatmeal");
            this.viewModel.getSelectedQuantityCategoryProperty().set(QuantityCategory.SERVING);
            this.viewModel.getCaloriesProperty().set(150);

            assertThrows(RuntimeException.class, () -> this.viewModel.createBaseFood());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateBaseFood_serverUnreachable_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenThrow(new Exception("connection refused"));

            this.viewModel.getDescriptionProperty().set("oatmeal");
            this.viewModel.getSelectedQuantityCategoryProperty().set(QuantityCategory.SERVING);
            this.viewModel.getCaloriesProperty().set(150);

            assertThrows(RuntimeException.class, () -> this.viewModel.createBaseFood());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateBaseFood_nullDescription_throwsException() {
        this.viewModel.getDescriptionProperty().set(null);
        this.viewModel.getSelectedQuantityCategoryProperty().set(QuantityCategory.SERVING);
        this.viewModel.getCaloriesProperty().set(150);

        assertThrows(Exception.class, () -> this.viewModel.createBaseFood());
    }

    @Test
    public void testCreateBaseFood_nullQuantityCategory_throwsException() {
        this.viewModel.getDescriptionProperty().set("oatmeal");
        this.viewModel.getSelectedQuantityCategoryProperty().set(null);
        this.viewModel.getCaloriesProperty().set(150);

        assertThrows(Exception.class, () -> this.viewModel.createBaseFood());
    }
}