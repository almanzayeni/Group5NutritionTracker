package edu.westga.cs3212.group5.nutritiontracker.viewmodel.createmealitempageviewmodel;

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
import edu.westga.cs3212.group5.nutritiontracker.viewmodel.CreateMealItemPageViewModel;

public class TestCreateMealItem {

	@TempDir
	Path tempDir;

	private BaseFood food1() {
		return new BaseFood("Food 1", QuantityCategory.WEIGHT, 1, 100, 10, 1, 1, 1, 1);
	}

	private BaseFood food2() {
		return new BaseFood("Food 2", QuantityCategory.QUANTITY, 1, 200, 20, 2, 2, 2, 2);
	}

	private CreateMealItemPageViewModel buildValidVm(Path filePath) {
		CreateMealItemPageViewModel vm = new CreateMealItemPageViewModel(filePath.toString());
		vm.getDescriptionProperty().set("Test Composite Food");
		vm.addFood(food1());
		vm.addFood(food2());
		return vm;
	}

	@Test
	void testcreateMealItemNullNameThrowsIllegalArgumentException() {
		Path file = tempDir.resolve("test.json");
		CreateMealItemPageViewModel vm = new CreateMealItemPageViewModel(file.toString());
		vm.getDescriptionProperty().set(null);
		vm.addFood(food1());

		assertThrows(IllegalArgumentException.class, vm::createMealItem);
	}

	@Test
	void testcreateMealItemEmptyNameThrowsIllegalArgumentException() {
		Path file = tempDir.resolve("test.json");
		CreateMealItemPageViewModel vm = new CreateMealItemPageViewModel(file.toString());
		vm.getDescriptionProperty().set("");
		vm.addFood(food1());

		assertThrows(IllegalArgumentException.class, vm::createMealItem);
	}

	@Test
	void testcreateMealItemNoIngredientsThrowsIllegalArgumentException() {
		Path file = tempDir.resolve("test.json");
		CreateMealItemPageViewModel vm = new CreateMealItemPageViewModel(file.toString());
		vm.getDescriptionProperty().set("Test Composite Food");

		assertThrows(IllegalArgumentException.class, vm::createMealItem);
	}

	@Test
	void testcreateMealItemShouldThrowJsonProcessingException() throws JsonProcessingException, IOException {
		Path file = tempDir.resolve("test.json");
		Files.createFile(file);

		ObjectMapper mockMapper = mock(ObjectMapper.class);
		when(mockMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Serialization failed") {
		});

		CreateMealItemPageViewModel vm = new CreateMealItemPageViewModel(mockMapper, file.toString());
		vm.getDescriptionProperty().set("Test Composite Food");
		vm.addFood(food1());
		vm.addFood(food2());

		assertThrows(JsonProcessingException.class, vm::createMealItem);
	}

	@Test
	void testcreateMealItemShouldThrowIOExceptionFromCheckForExistingFoodWhenPathIsDirectory() throws IOException {
		Path dirPath = tempDir.resolve("notAFile.json");
		Files.createDirectory(dirPath);

		CreateMealItemPageViewModel vm = buildValidVm(dirPath);

		assertThrows(IOException.class, vm::createMealItem);
	}

	@Test
	void testcreateMealItemShouldThrowIOExceptionOnWrite() throws IOException {
		Path file = tempDir.resolve("test.json");
		Files.createFile(file);

		CreateMealItemPageViewModel vm = buildValidVm(file);

		try (MockedStatic<Files> filesMock = mockStatic(Files.class, CALLS_REAL_METHODS)) {
			filesMock.when(() -> Files.write(eq(file), any(byte[].class), eq(StandardOpenOption.CREATE),
					eq(StandardOpenOption.APPEND))).thenThrow(new IOException("Forced write failure"));

			assertThrows(IOException.class, vm::createMealItem);
		}
	}

	@Test
	void testcreateMealItemDuplicateNameThrowsIllegalArgumentExceptionAndCoversBadJsonLineInCheck()
			throws Exception {
		Path file = tempDir.resolve("test.json");
		Files.createFile(file);

		Files.writeString(file, "not-json" + System.lineSeparator(), StandardOpenOption.APPEND);

		CompositeFood existing = new CompositeFood();
		existing.setDescription("Test Composite Food");
		existing.setQuantityCategory(QuantityCategory.QUANTITY);
		existing.addIngredient(food1());
		existing.addIngredient(food2());

		ObjectMapper mapper = new ObjectMapper();
		String validLine = mapper.writeValueAsString(existing);
		Files.writeString(file, validLine + System.lineSeparator(), StandardOpenOption.APPEND);

		CreateMealItemPageViewModel vm = buildValidVm(file);

		assertThrows(IllegalArgumentException.class, vm::createMealItem);
	}

	@Test
	void testCreateValidCompositeFoodSuccessAndFieldsCleared() throws Exception {
		Path file = tempDir.resolve("test.json");
		Files.createFile(file);

		CreateMealItemPageViewModel vm = buildValidVm(file);

		vm.createMealItem();

		List<String> lines = Files.readAllLines(file);
		assertEquals(1, lines.size());

		assertEquals("", vm.getDescriptionProperty().get());
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