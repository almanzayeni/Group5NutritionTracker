package edu.westga.cs3212.group5.nutritiontracker.model.compositefood;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.westga.cs3212.group5.nutritiontracker.model.BaseFood;
import edu.westga.cs3212.group5.nutritiontracker.model.CompositeFood;
import edu.westga.cs3212.group5.nutritiontracker.model.FoodItem;
import edu.westga.cs3212.group5.nutritiontracker.model.QuantityCategory;

public class TestCompositeFoodConstructor {
	BaseFood ingredient1;
	BaseFood ingredient2;
	CompositeFood compositeFoodIngredient1;
	CompositeFood compositeFoodIngredient2;
	List<FoodItem> baseIngredients;
	List<FoodItem> compositeIngredients;
	List<FoodItem> allIngredients;
	
	@BeforeEach
	public void setUp() {
		BaseFood ingredient1 = new BaseFood("Ingredient 1", QuantityCategory.SERVING, 200);
		BaseFood ingredient2 = new BaseFood("Ingredient 2", QuantityCategory.WEIGHT, 2, 100, 1, 2, 3, 4, 5);
		
		baseIngredients = new ArrayList<FoodItem>();
		baseIngredients.add(ingredient1);
		baseIngredients.add(ingredient2);
		
		compositeFoodIngredient1 = new CompositeFood("Composite Ingredient", QuantityCategory.QUANTITY, baseIngredients);
		compositeFoodIngredient2 = new CompositeFood("Composite Ingredient 2", QuantityCategory.SERVING, 2, baseIngredients);
		
		compositeIngredients = new ArrayList<FoodItem>();
		compositeIngredients.add(compositeFoodIngredient1);
		compositeIngredients.add(compositeFoodIngredient2);
		
		allIngredients = new ArrayList<FoodItem>();
		allIngredients.addAll(baseIngredients);
		allIngredients.addAll(compositeIngredients);
	}
	
	@Test
	public void testNullIngredients() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood("Composite Food", QuantityCategory.SERVING, null);
		});
	}
	
	@Test
	public void testFullParameterNullIngredients() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood("Composite Food", QuantityCategory.SERVING, 1, null);
		});
	}
	
	@Test
	public void testEmptyIngredients() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood("Composite Food", QuantityCategory.SERVING, new ArrayList<FoodItem>());
		});
	}
	
	@Test
	public void testFullParameterEmptyIngredients() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood("Composite Food", QuantityCategory.SERVING, 1, new ArrayList<FoodItem>());
		});
	}
	
	@Test
	public void testFullParameterDuplicateIngredientDescriptions() {
		baseIngredients.add(new BaseFood("Ingredient 1", QuantityCategory.SERVING, 200));
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood("Composite Food", QuantityCategory.SERVING, 1, baseIngredients);
		});
	}
	
	@Test
	public void testNullDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood(null, QuantityCategory.SERVING, baseIngredients);
		});
	}
	
	@Test
	public void testBlankDescription() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood("   ", QuantityCategory.SERVING, baseIngredients);
		});
	}
	
	@Test
	public void testNullQuantityCategory() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood("Composite Food", null, baseIngredients);
		});
	}
	
	@Test
	public void testPortionSizeLessThan1() {
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood("Composite Food", QuantityCategory.SERVING, 0, baseIngredients);
		});
	}
	
	@Test
	public void testDuplicateIngredientDescriptions() {
		baseIngredients.add(new BaseFood("Ingredient 1", QuantityCategory.SERVING, 200));
		assertThrows(IllegalArgumentException.class, () -> {
			new CompositeFood("Composite Food", QuantityCategory.SERVING, baseIngredients);
		});
	}
	
	@Test
	public void testValidConstructorWithoutPortionSizeOnlyBaseIngredients() {
		CompositeFood compositeFood = new CompositeFood("Composite Food", QuantityCategory.SERVING, baseIngredients);
		
		assertEquals("Composite Food", compositeFood.getDescription());
		assertEquals(QuantityCategory.SERVING, compositeFood.getQuantityCategory());
		assertEquals(2, compositeFood.getIngredients().size());
		assertEquals(1, compositeFood.getPortionSize(), 0.001);
		assertEquals(400, compositeFood.getCalories(), 0.001);
		assertEquals(2, compositeFood.getProtein(), 0.001);
		assertEquals(4, compositeFood.getFat(), 0.001);
		assertEquals(6, compositeFood.getSugar(), 0.001);
		assertEquals(8, compositeFood.getCarbohydrates(), 0.001);
		assertEquals(10, compositeFood.getSodium(), 0.001);
	}
	
	@Test
	public void testValidConstructorWithPortionSizeOneOnlyBaseIngredients() {
		CompositeFood compositeFood = new CompositeFood("Composite Food", QuantityCategory.SERVING, 1, baseIngredients);
		
		assertEquals("Composite Food", compositeFood.getDescription());
		assertEquals(QuantityCategory.SERVING, compositeFood.getQuantityCategory());
		assertEquals(2, compositeFood.getIngredients().size());
		assertEquals(1, compositeFood.getPortionSize(), 0.001);
		assertEquals(400, compositeFood.getCalories(), 0.001);
		assertEquals(2, compositeFood.getProtein(), 0.001);
		assertEquals(4, compositeFood.getFat(), 0.001);
		assertEquals(6, compositeFood.getSugar(), 0.001);
		assertEquals(8, compositeFood.getCarbohydrates(), 0.001);
		assertEquals(10, compositeFood.getSodium(), 0.001);
	}
	
	@Test
	public void testValidConstructorWithPortionSizeTwoOnlyBaseIngredients() {
		CompositeFood compositeFood = new CompositeFood("Composite Food", QuantityCategory.SERVING, 2, baseIngredients);
		
		assertEquals("Composite Food", compositeFood.getDescription());
		assertEquals(QuantityCategory.SERVING, compositeFood.getQuantityCategory());
		assertEquals(2, compositeFood.getIngredients().size());
		assertEquals(2, compositeFood.getPortionSize(), 0.001);
		assertEquals(800, compositeFood.getCalories(), 0.001);
		assertEquals(4, compositeFood.getProtein(), 0.001);
		assertEquals(8, compositeFood.getFat(), 0.001);
		assertEquals(12, compositeFood.getSugar(), 0.001);
		assertEquals(16, compositeFood.getCarbohydrates(), 0.001);
		assertEquals(20, compositeFood.getSodium(), 0.001);
	}
	
	@Test
	public void testValidConstructorWithoutPortionSizeWithOnlyCompositeIngredients() {
		CompositeFood compositeFood = new CompositeFood("Composite Food", QuantityCategory.SERVING, compositeIngredients);
		
		assertEquals("Composite Food", compositeFood.getDescription());
		assertEquals(QuantityCategory.SERVING, compositeFood.getQuantityCategory());
		assertEquals(2, compositeFood.getIngredients().size());
		assertEquals(1, compositeFood.getPortionSize(), 0.001);
		assertEquals(1200, compositeFood.getCalories(), 0.001);
		assertEquals(6, compositeFood.getProtein(), 0.001);
		assertEquals(12, compositeFood.getFat(), 0.001);
		assertEquals(18, compositeFood.getSugar(), 0.001);
		assertEquals(24, compositeFood.getCarbohydrates(), 0.001);
		assertEquals(30, compositeFood.getSodium(), 0.001);
	}
	
	@Test
	public void testValidConstructorWithPortionSizeOfOneWithOnlyCompositeIngredients() {
		CompositeFood compositeFood = new CompositeFood("Composite Food", QuantityCategory.SERVING, 1, compositeIngredients);
		
		assertEquals("Composite Food", compositeFood.getDescription());
		assertEquals(QuantityCategory.SERVING, compositeFood.getQuantityCategory());
		assertEquals(2, compositeFood.getIngredients().size());
		assertEquals(1, compositeFood.getPortionSize(), 0.001);
		assertEquals(1200, compositeFood.getCalories(), 0.001);
		assertEquals(6, compositeFood.getProtein(), 0.001);
		assertEquals(12, compositeFood.getFat(), 0.001);
		assertEquals(18, compositeFood.getSugar(), 0.001);
		assertEquals(24, compositeFood.getCarbohydrates(), 0.001);
		assertEquals(30, compositeFood.getSodium(), 0.001);
	}
	
	@Test
	public void testValidConstructorWithPortionSizeOfTwoWithOnlyCompositeIngredients() {
		CompositeFood compositeFood = new CompositeFood("Composite Food", QuantityCategory.SERVING, 2, compositeIngredients);
		
		assertEquals("Composite Food", compositeFood.getDescription());
		assertEquals(QuantityCategory.SERVING, compositeFood.getQuantityCategory());
		assertEquals(2, compositeFood.getIngredients().size());
		assertEquals(2, compositeFood.getPortionSize(), 0.001);
		assertEquals(2400, compositeFood.getCalories(), 0.001);
		assertEquals(12, compositeFood.getProtein(), 0.001);
		assertEquals(24, compositeFood.getFat(), 0.001);
		assertEquals(36, compositeFood.getSugar(), 0.001);
		assertEquals(48, compositeFood.getCarbohydrates(), 0.001);
		assertEquals(60, compositeFood.getSodium(), 0.001);
	}
	
	@Test
	public void testValidConstructorWithoutPortionSizeWithBaseAndCompositeIngredients() {
		CompositeFood compositeFood = new CompositeFood("Composite Food", QuantityCategory.SERVING, allIngredients);
		
		assertEquals("Composite Food", compositeFood.getDescription());
		assertEquals(QuantityCategory.SERVING, compositeFood.getQuantityCategory());
		assertEquals(4, compositeFood.getIngredients().size());
		assertEquals(1, compositeFood.getPortionSize(), 0.001);
		assertEquals(1600, compositeFood.getCalories(), 0.001);
		assertEquals(8, compositeFood.getProtein(), 0.001);
		assertEquals(16, compositeFood.getFat(), 0.001);
		assertEquals(24, compositeFood.getSugar(), 0.001);
		assertEquals(32, compositeFood.getCarbohydrates(), 0.001);
		assertEquals(40, compositeFood.getSodium(), 0.001);
	}
	
	@Test
	public void testValidConstructorWithPortionSizeOfOneWithBaseAndCompositeIngredients() {
		CompositeFood compositeFood = new CompositeFood("Composite Food", QuantityCategory.SERVING, 1, allIngredients);
		
		assertEquals("Composite Food", compositeFood.getDescription());
		assertEquals(QuantityCategory.SERVING, compositeFood.getQuantityCategory());
		assertEquals(4, compositeFood.getIngredients().size());
		assertEquals(1, compositeFood.getPortionSize(), 0.001);
		assertEquals(1600, compositeFood.getCalories(), 0.001);
		assertEquals(8, compositeFood.getProtein(), 0.001);
		assertEquals(16, compositeFood.getFat(), 0.001);
		assertEquals(24, compositeFood.getSugar(), 0.001);
		assertEquals(32, compositeFood.getCarbohydrates(), 0.001);
		assertEquals(40, compositeFood.getSodium(), 0.001);
	}
	
	@Test
	public void testValidConstructorWithPortionSizeOfTwoWithBaseAndCompositeIngredients() {
		CompositeFood compositeFood = new CompositeFood("Composite Food", QuantityCategory.SERVING, 2, allIngredients);
		
		assertEquals("Composite Food", compositeFood.getDescription());
		assertEquals(QuantityCategory.SERVING, compositeFood.getQuantityCategory());
		assertEquals(4, compositeFood.getIngredients().size());
		assertEquals(2, compositeFood.getPortionSize(), 0.001);
		assertEquals(3200, compositeFood.getCalories(), 0.001);
		assertEquals(16, compositeFood.getProtein(), 0.001);
		assertEquals(32, compositeFood.getFat(), 0.001);
		assertEquals(48, compositeFood.getSugar(), 0.001);
		assertEquals(64, compositeFood.getCarbohydrates(), 0.001);
		assertEquals(80, compositeFood.getSodium(), 0.001);
	}
}
