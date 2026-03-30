'''
Created on Mar 30, 2026

@author: OpenAI
'''
import unittest

from model.base_food import BaseFood
from model.composite_food import CompositeFood
from model.quantity_category import QuantityCategory


class TestConstructor(unittest.TestCase):

    def test_sets_all_constructor_values(self):
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
        rice = BaseFood(
            "rice",
            QuantityCategory.SERVING,
            1,
            205,
            4,
            0,
            0,
            45,
            1,
        )

        food = CompositeFood(
            "chicken rice bowl",
            QuantityCategory.SERVING,
            1,
            485,
            57,
            6,
            0,
            45,
            126,
            {
                chicken.get_description(): chicken,
                rice.get_description(): rice,
            },
        )

        self.assertEqual("chicken rice bowl", food.get_description())
        self.assertEqual(QuantityCategory.SERVING, food.get_quantity_category())
        self.assertEqual(1, food.get_portion_size())
        self.assertEqual(485, food.get_calories())
        self.assertEqual(57, food.get_protein())
        self.assertEqual(6, food.get_fat())
        self.assertEqual(0, food.get_sugar())
        self.assertEqual(45, food.get_carbohydrates())
        self.assertEqual(126, food.get_sodium())
        self.assertEqual(
            {
                chicken.get_description(): chicken,
                rice.get_description(): rice,
            },
            food.get_ingredients(),
        )

    def test_keeps_empty_and_zero_like_values_without_modifying_them(self):
        food = CompositeFood(
            "",
            QuantityCategory.QUANTITY,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            {},
        )

        self.assertEqual("", food.get_description())
        self.assertEqual(QuantityCategory.QUANTITY, food.get_quantity_category())
        self.assertEqual(0, food.get_portion_size())
        self.assertEqual(0, food.get_calories())
        self.assertEqual(0, food.get_protein())
        self.assertEqual(0, food.get_fat())
        self.assertEqual(0, food.get_sugar())
        self.assertEqual(0, food.get_carbohydrates())
        self.assertEqual(0, food.get_sodium())
        self.assertEqual({}, food.get_ingredients())


if __name__ == "__main__":
    unittest.main()
