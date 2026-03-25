'''
Created on Mar 25, 2026

@author: Justin Smith
'''
import unittest

from model.base_food import BaseFood
from model.quantity_category import QuantityCategory


class TestToDict(unittest.TestCase):

    def test_returns_dictionary_with_expected_keys_and_values(self):
        food = BaseFood(
            "banana",
            QuantityCategory.QUANTITY,
            1,
            105,
            1,
            0,
            14,
            27,
            1,
        )

        self.assertEqual(
            {
                "type": "base",
                "description": "banana",
                "quantityCategory": "QUANTITY",
                "portionSize": 1,
                "calories": 105,
                "protein": 1,
                "fat": 0,
                "sugar": 14,
                "carbohydrates": 27,
                "sodium": 1,
            },
            food.to_dict(),
        )

    def test_uses_enum_value_for_different_quantity_category(self):
        food = BaseFood(
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

        result = food.to_dict()

        self.assertEqual("base", result["type"])
        self.assertEqual("WEIGHT", result["quantityCategory"])
        self.assertEqual("chicken breast", result["description"])
        self.assertEqual(6, result["portionSize"])
        self.assertEqual(280, result["calories"])
        self.assertEqual(53, result["protein"])
        self.assertEqual(6, result["fat"])
        self.assertEqual(0, result["sugar"])
        self.assertEqual(0, result["carbohydrates"])
        self.assertEqual(125, result["sodium"])


if __name__ == "__main__":
    unittest.main()
