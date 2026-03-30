'''
Created on Mar 30, 2026

@author: OpenAI
'''
import unittest

from model.food_item import FoodItem


class _DelegatingFoodItem(FoodItem):

    def get_description(self):
        return FoodItem.get_description(self)

    def get_quantity_category(self):
        return FoodItem.get_quantity_category(self)

    def get_portion_size(self):
        return FoodItem.get_portion_size(self)

    def get_calories(self):
        return FoodItem.get_calories(self)

    def get_protein(self):
        return FoodItem.get_protein(self)

    def get_fat(self):
        return FoodItem.get_fat(self)

    def get_sugar(self):
        return FoodItem.get_sugar(self)

    def get_carbohydrates(self):
        return FoodItem.get_carbohydrates(self)

    def get_sodium(self):
        return FoodItem.get_sodium(self)


class TestFoodItem(unittest.TestCase):

    def test_abstract_methods_default_to_none_when_called_directly(self):
        food_item = _DelegatingFoodItem()

        self.assertIsNone(food_item.get_description())
        self.assertIsNone(food_item.get_quantity_category())
        self.assertIsNone(food_item.get_portion_size())
        self.assertIsNone(food_item.get_calories())
        self.assertIsNone(food_item.get_protein())
        self.assertIsNone(food_item.get_fat())
        self.assertIsNone(food_item.get_sugar())
        self.assertIsNone(food_item.get_carbohydrates())
        self.assertIsNone(food_item.get_sodium())


if __name__ == "__main__":
    unittest.main()
