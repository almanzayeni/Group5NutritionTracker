'''
Created on Mar 30, 2026

@author: OpenAI
'''
import unittest

from model.base_food import BaseFood
from model.composite_food import CompositeFood
from model.quantity_category import QuantityCategory


class TestToDict(unittest.TestCase):

    def test_returns_dictionary_with_serialized_ingredients(self):
        chicken = BaseFood(
            "chicken breast",
            QuantityCategory.WEIGHT,
            6,
            280,
            53,
            6,
            0,
            0,
            125,
        )

        food = CompositeFood(
            "meal prep bowl",
            QuantityCategory.SERVING,
            1,
            480,
            53,
            6,
            0,
            45,
            125,
            {
                "protein": chicken,
                "note": 2,
            },
        )

        self.assertEqual(
            {
                "type": "composite",
                "description": "meal prep bowl",
                "quantityCategory": "SERVING",
                "portionSize": 1,
                "calories": 480,
                "protein": 53,
                "fat": 6,
                "sugar": 0,
                "carbohydrates": 45,
                "sodium": 125,
                "ingredients": {
                    "protein": chicken.to_dict(),
                    "note": "2",
                },
            },
            food.to_dict(),
        )

    def test_uses_string_conversion_for_non_food_ingredients(self):
        food = CompositeFood(
            "custom trail mix",
            QuantityCategory.WEIGHT,
            3,
            320,
            8,
            18,
            12,
            22,
            40,
            {"servingNote": ["shareable"]},
        )

        result = food.to_dict()

        self.assertEqual("composite", result["type"])
        self.assertEqual("WEIGHT", result["quantityCategory"])
        self.assertEqual("['shareable']", result["ingredients"]["servingNote"])


if __name__ == "__main__":
    unittest.main()
