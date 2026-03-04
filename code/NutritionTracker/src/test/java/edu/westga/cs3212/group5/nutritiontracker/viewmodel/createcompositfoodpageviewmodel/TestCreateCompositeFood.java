package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createcompositfoodpageviewmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateCompositeFoodPageViewModel;

public class TestCreateCompositeFood {

	@TempDir
	Path tempDir;

	private BaseFood ingredient1() {
		return new BaseFood("Ingredient 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
	}

	private BaseFood ingredient2() {
		return new BaseFood("Ingredient 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
	}

	private CreateCompositeFoodPageViewModel buildValidVm(Path filePath) {
		CreateCompositeFoodPageViewModel vm = new CreateCompositeFoodPageViewModel(filePath.toString());
		vm.getDescriptionProperty().set("Test Composite Food");
		vm.getSelectedQuantityCategoryProperty().set(QuantityCategory.QUANTITY);
		vm.addIngredient(ingredient1());
		vm.addIngredient(ingredient2());
		return vm;
	}

	@Test
	void testCreateCompositeFoodNullNameThrowsIllegalArgumentException() {
		Path file = tempDir.resolve("test.json");
		CreateCompositeFoodPageViewModel vm = new CreateCompositeFoodPageViewModel(file.toString());
		vm.getDescriptionProperty().set(null);
		vm.getSelectedQuantityCategoryProperty().set(QuantityCategory.QUANTITY);
		vm.addIngredient(ingredient1());

		assertThrows(IllegalArgumentException.class, vm::createCompositeFood);
	}

	@Test
	void testCreateCompositeFoodEmptyNameThrowsIllegalArgumentException() {
		Path file = tempDir.resolve("test.json");
		CreateCompositeFoodPageViewModel vm = new CreateCompositeFoodPageViewModel(file.toString());
		vm.getDescriptionProperty().set("");
		vm.getSelectedQuantityCategoryProperty().set(QuantityCategory.QUANTITY);
		vm.addIngredient(ingredient1());

		assertThrows(IllegalArgumentException.class, vm::createCompositeFood);
	}

	@Test
	void testCreateCompositeFoodNoIngredientsThrowsIllegalArgumentException() {
		Path file = tempDir.resolve("test.json");
		CreateCompositeFoodPageViewModel vm = new CreateCompositeFoodPageViewModel(file.toString());
		vm.getDescriptionProperty().set("Test Composite Food");
		vm.getSelectedQuantityCategoryProperty().set(QuantityCategory.QUANTITY);

		assertThrows(IllegalArgumentException.class, vm::createCompositeFood);
	}

	@Test
	void testCreateCompositeFoodNullQuantityCategoryThrowsIllegalArgumentException() {
		Path file = tempDir.resolve("test.json");
		CreateCompositeFoodPageViewModel vm = new CreateCompositeFoodPageViewModel(file.toString());
		vm.getDescriptionProperty().set("Test Composite Food");
		vm.getSelectedQuantityCategoryProperty().set(null);
		vm.addIngredient(ingredient1());

		assertThrows(IllegalArgumentException.class, vm::createCompositeFood);
	}

	@Test
	void testCreateCompositeFoodShouldThrowJsonProcessingException() throws JsonProcessingException, IOException {
		Path file = tempDir.resolve("test.json");
		Files.createFile(file);

		ObjectMapper mockMapper = mock(ObjectMapper.class);
		when(mockMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Serialization failed") {
		});

		CreateCompositeFoodPageViewModel vm = new CreateCompositeFoodPageViewModel(mockMapper, file.toString());
		vm.getDescriptionProperty().set("Test Composite Food");
		vm.getSelectedQuantityCategoryProperty().set(QuantityCategory.QUANTITY);
		vm.addIngredient(ingredient1());
		vm.addIngredient(ingredient2());

		assertThrows(JsonProcessingException.class, vm::createCompositeFood);
	}

	@Test
	void testCreateCompositeFoodShouldThrowIOExceptionFromCheckForExistingFoodWhenPathIsDirectory() throws IOException {
		Path dirPath = tempDir.resolve("notAFile.json");
		Files.createDirectory(dirPath);

		CreateCompositeFoodPageViewModel vm = buildValidVm(dirPath);

		assertThrows(IOException.class, vm::createCompositeFood);
	}

	@Test
	void testCreateCompositeFoodShouldThrowIOExceptionOnWrite() throws IOException {
		Path file = tempDir.resolve("test.json");
		Files.createFile(file);

		CreateCompositeFoodPageViewModel vm = buildValidVm(file);

		try (MockedStatic<Files> filesMock = mockStatic(Files.class, CALLS_REAL_METHODS)) {
			filesMock.when(() -> Files.write(eq(file), any(byte[].class), eq(StandardOpenOption.CREATE),
					eq(StandardOpenOption.APPEND))).thenThrow(new IOException("Forced write failure"));

			assertThrows(IOException.class, vm::createCompositeFood);
		}
	}

	@Test
	void testCreateCompositeFoodDuplicateNameThrowsIllegalArgumentExceptionAndCoversBadJsonLineInCheck()
			throws Exception {
		Path file = tempDir.resolve("test.json");
		Files.createFile(file);

		Files.writeString(file, "not-json" + System.lineSeparator(), StandardOpenOption.APPEND);

		CompositeFood existing = new CompositeFood();
		existing.setDescription("Test Composite Food");
		existing.setQuantityCategory(QuantityCategory.QUANTITY);
		existing.addIngredient(ingredient1());
		existing.addIngredient(ingredient2());

		ObjectMapper mapper = new ObjectMapper();
		String validLine = mapper.writeValueAsString(existing);
		Files.writeString(file, validLine + System.lineSeparator(), StandardOpenOption.APPEND);

		CreateCompositeFoodPageViewModel vm = buildValidVm(file);

		assertThrows(IllegalArgumentException.class, vm::createCompositeFood);
	}

	@Test
	void testCreateValidCompositeFoodSuccessAndFieldsCleared() throws Exception {
		Path file = tempDir.resolve("test.json");
		Files.createFile(file);

		CreateCompositeFoodPageViewModel vm = buildValidVm(file);

		vm.createCompositeFood();

		List<String> lines = Files.readAllLines(file);
		assertEquals(1, lines.size());

		assertEquals("", vm.getDescriptionProperty().get());
		assertEquals(null, vm.getSelectedQuantityCategoryProperty().get());
		assertTrue(vm.getIngredientsListProperty().isEmpty());
		assertEquals(0.0, vm.getTotalCaloriesProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalProteinProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalFatProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalSugarProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalCarbohydratesProperty().get(), 0.0001);
		assertEquals(0.0, vm.getTotalSodiumProperty().get(), 0.0001);

		CompositeFood created = new ObjectMapper().readValue(lines.get(0), CompositeFood.class);
		assertEquals("Test Composite Food", created.getDescription());
		assertEquals(300.0, created.getCalories(), 0.001);

		FoodItem i1 = created.getIngredientByDescription("Ingredient 1");
		FoodItem i2 = created.getIngredientByDescription("Ingredient 2");
		assertTrue(i1 != null);
		assertTrue(i2 != null);
	}
}