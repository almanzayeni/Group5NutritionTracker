'''
Created on Mar 22, 2026

@author: Justin Smith
'''
from model.food_item import FoodItem

class CompositeFood(FoodItem):
    TYPE = "composite"
    
    def __init__(
        self,
        description,
        quantity_category,
        portion_size,
        calories,
        protein,
        fat,
        sugar,
        carbohydrates,
        sodium,
        ingredients
    ):
        self._description = description
        self._quantity_category = quantity_category
        self._portion_size = portion_size
        self._calories = calories
        self._protein = protein
        self._fat = fat
        self._sugar = sugar
        self._carbohydrates = carbohydrates
        self._sodium = sodium
        self._ingredients = ingredients

    def get_description(self):
        return self._description

    def get_quantity_category(self):
        return self._quantity_category

    def get_portion_size(self):
        return self._portion_size

    def get_calories(self):
        return self._calories

    def get_protein(self):
        return self._protein

    def get_fat(self):
        return self._fat

    def get_sugar(self):
        return self._sugar

    def get_carbohydrates(self):
        return self._carbohydrates

    def get_sodium(self):
        return self._sodium
    
    def get_ingredients(self):
        return self._ingredients
    
    def to_dict(self):
        return {
            "type": self.TYPE,
            "description": self._description,
            "quantityCategory": self._quantity_category.value,
            "portionSize": self._portion_size,
            "calories": self._calories,
            "protein": self._protein,
            "fat": self._fat,
            "sugar": self._sugar,
            "carbohydrates": self._carbohydrates,
            "sodium": self._sodium,
            "ingredients": {
                key: value.to_dict() if hasattr(value, "to_dict") else str(value)
                for key, value in self._ingredients.items()
            }
        }