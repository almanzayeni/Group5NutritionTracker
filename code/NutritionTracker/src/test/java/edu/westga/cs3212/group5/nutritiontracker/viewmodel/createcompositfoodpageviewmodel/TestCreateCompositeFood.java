package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createcompositfoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.server.ServerClient;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositeFoodPageViewModel;

/**
 * Tests for CreateCompositeFoodPageViewModel.
 *
 * @author (your name)
 * @version Spring 2026
 */
public class TestCreateCompositeFood {

    private CreateCompositeFoodPageViewModel viewModel;

    @BeforeEach
    public void setUp() {
        this.viewModel = new CreateCompositeFoodPageViewModel();
    }

    private FoodItem makeIngredient(String description) {
        return new BaseFood(description, QuantityCategory.SERVING, 1, 100, 5, 3, 1, 20, 0);
    }

    private void setUpValidViewModelWithIngredient() {
        this.viewModel.getDescriptionProperty().set("chicken salad");
        this.viewModel.getSelectedQuantityCategoryProperty().set(QuantityCategory.SERVING);
        this.viewModel.addIngredient(makeIngredient("chicken breast"));
    }

    @Test
    public void testConstructor_descriptionPropertyInitializedEmpty() {
        assertTrue(this.viewModel.getDescriptionProperty().get() == null
                || this.viewModel.getDescriptionProperty().get().isEmpty());
    }

    @Test
    public void testConstructor_quantityCategoriesListContainsThreeItems() {
        assertEquals(3, this.viewModel.getQuantityCategoriesListPropery().size());
    }

    @Test
    public void testConstructor_ingredientsListInitializedEmpty() {
        assertTrue(this.viewModel.getIngredientsListProperty().isEmpty());
    }

    @Test
    public void testConstructor_totalCaloriesInitializedToZero() {
        assertEquals(0.0, this.viewModel.getTotalCaloriesProperty().get());
    }

    @Test
    public void testConstructor_portionSizeDefaultsToOne() {
        assertEquals(1.0, this.viewModel.getPortionSize());
    }

    @Test
    public void testAddIngredient_nullIngredient_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.addIngredient(null);
        });
    }

    @Test
    public void testAddIngredient_duplicateIngredient_throwsIllegalArgumentException() {
        FoodItem ingredient = makeIngredient("chicken breast");
        this.viewModel.addIngredient(ingredient);

        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.addIngredient(ingredient);
        });
    }

    @Test
    public void testAddIngredient_validIngredient_ingredientAppearsInList() {
        FoodItem ingredient = makeIngredient("chicken breast");
        this.viewModel.addIngredient(ingredient);

        assertTrue(this.viewModel.getIngredientsListProperty().contains(ingredient));
    }

    @Test
    public void testAddIngredient_validIngredient_totalCaloriesUpdated() {
        FoodItem ingredient = makeIngredient("chicken breast");
        this.viewModel.addIngredient(ingredient);

        assertEquals(ingredient.getCalories(), this.viewModel.getTotalCaloriesProperty().get());
    }

    @Test
    public void testRemoveIngredient_nullIngredient_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.removeIngredient(null);
        });
    }

    @Test
    public void testRemoveIngredient_ingredientNotInList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.removeIngredient(makeIngredient("salad"));
        });
    }

    @Test
    public void testRemoveIngredient_validIngredient_ingredientRemovedFromList() {
        FoodItem ingredient = makeIngredient("chicken breast");
        this.viewModel.addIngredient(ingredient);
        this.viewModel.removeIngredient(ingredient);

        assertFalse(this.viewModel.getIngredientsListProperty().contains(ingredient));
    }

    @Test
    public void testRemoveIngredient_validIngredient_totalCaloriesDecreasedToZero() {
        FoodItem ingredient = makeIngredient("chicken breast");
        this.viewModel.addIngredient(ingredient);
        this.viewModel.removeIngredient(ingredient);

        assertEquals(0.0, this.viewModel.getTotalCaloriesProperty().get(), 0.001);
    }

    @Test
    public void testAddIngredients_nullList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.addIngredients(null);
        });
    }

    @Test
    public void testAddIngredients_emptyList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.addIngredients(List.of());
        });
    }

    @Test
    public void testAddIngredients_validList_allIngredientsAdded() {
        List<FoodItem> ingredients = List.of(
                makeIngredient("chicken breast"),
                makeIngredient("salad"));
        this.viewModel.addIngredients(ingredients);

        assertEquals(2, this.viewModel.getIngredientsListProperty().size());
    }

    @Test
    public void testGetIngredient_nullIngredient_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.getIngredient(null);
        });
    }

    @Test
    public void testGetIngredient_ingredientExists_returnsIngredient() {
        FoodItem ingredient = makeIngredient("chicken breast");
        this.viewModel.addIngredient(ingredient);

        FoodItem result = this.viewModel.getIngredient(ingredient);
        assertEquals(ingredient.getDescription(), result.getDescription());
    }

    @Test
    public void testGetIngredient_ingredientDoesNotExist_returnsNull() {
        FoodItem result = this.viewModel.getIngredient(makeIngredient("salad"));
        assertNull(result);
    }

    @Test
    public void testCreateCompositeFood_nullDescription_throwsIllegalArgumentException() {
        this.viewModel.getDescriptionProperty().set(null);
        this.viewModel.getSelectedQuantityCategoryProperty().set(QuantityCategory.SERVING);
        this.viewModel.addIngredient(makeIngredient("chicken breast"));

        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.createCompositeFood();
        });
    }

    @Test
    public void testCreateCompositeFood_emptyDescription_throwsIllegalArgumentException() {
        this.viewModel.getDescriptionProperty().set("");
        this.viewModel.getSelectedQuantityCategoryProperty().set(QuantityCategory.SERVING);
        this.viewModel.addIngredient(makeIngredient("chicken breast"));

        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.createCompositeFood();
        });
    }

    @Test
    public void testCreateCompositeFood_noIngredients_throwsIllegalArgumentException() {
        this.viewModel.getDescriptionProperty().set("chicken salad");
        this.viewModel.getSelectedQuantityCategoryProperty().set(QuantityCategory.SERVING);

        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.createCompositeFood();
        });
    }

    @Test
    public void testCreateCompositeFood_nullQuantityCategory_throwsIllegalArgumentException() {
        this.viewModel.getDescriptionProperty().set("chicken salad");
        this.viewModel.getSelectedQuantityCategoryProperty().set(null);
        this.viewModel.addIngredient(makeIngredient("chicken breast"));

        assertThrows(IllegalArgumentException.class, () -> {
            this.viewModel.createCompositeFood();
        });
    }

    @Test
    public void testCreateCompositeFood_validFood_serverCalledSuccessfully() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"1\"}");

            setUpValidViewModelWithIngredient();

            assertDoesNotThrow(() -> this.viewModel.createCompositeFood());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompositeFood_success_fieldsCleared() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"1\"}");

            setUpValidViewModelWithIngredient();
            this.viewModel.createCompositeFood();

            assertTrue(this.viewModel.getDescriptionProperty().get() == null
                    || this.viewModel.getDescriptionProperty().get().isEmpty());
            assertTrue(this.viewModel.getIngredientsListProperty().isEmpty());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompositeFood_serverReturnsFailure_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenReturn("{\"status\":\"-1\",\"failure_message\":\"food already exists\"}");

            setUpValidViewModelWithIngredient();

            assertThrows(RuntimeException.class, () -> this.viewModel.createCompositeFood());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateCompositeFood_serverUnreachable_throwsRuntimeException() {
        try (MockedStatic<ServerClient> mockClient = mockStatic(ServerClient.class)) {
            mockClient.when(() -> ServerClient.send(anyString()))
                      .thenThrow(new Exception("connection refused"));

            setUpValidViewModelWithIngredient();

            assertThrows(RuntimeException.class, () -> this.viewModel.createCompositeFood());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}