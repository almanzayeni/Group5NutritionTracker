'''
Created on Mar 25, 2026

@author: Justin Smith
'''
import unittest

from model.base_food import BaseFood
from model.quantity_category import QuantityCategory


class TestConstructor(unittest.TestCase):

    def test_sets_all_constructor_values(self):
        food = BaseFood(
            "greek yogurt",
            QuantityCategory.SERVING,
            2,
            140,
            12,
            0,
            6,
            8,
            55,
        )

        self.assertEqual("greek yogurt", food.get_description())
        self.assertEqual(QuantityCategory.SERVING, food.get_quantity_category())
        self.assertEqual(2, food.get_portion_size())
        self.assertEqual(140, food.get_calories())
        self.assertEqual(12, food.get_protein())
        self.assertEqual(0, food.get_fat())
        self.assertEqual(6, food.get_sugar())
        self.assertEqual(8, food.get_carbohydrates())
        self.assertEqual(55, food.get_sodium())

    def test_keeps_boundary_like_values_without_modifying_them(self):
        food = BaseFood(
            "",
            QuantityCategory.WEIGHT,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
        )

        self.assertEqual("", food.get_description())
        self.assertEqual(QuantityCategory.WEIGHT, food.get_quantity_category())
        self.assertEqual(0, food.get_portion_size())
        self.assertEqual(0, food.get_calories())
        self.assertEqual(0, food.get_protein())
        self.assertEqual(0, food.get_fat())
        self.assertEqual(0, food.get_sugar())
        self.assertEqual(0, food.get_carbohydrates())
        self.assertEqual(0, food.get_sodium())


if __name__ == "__main__":
    unittest.main()
