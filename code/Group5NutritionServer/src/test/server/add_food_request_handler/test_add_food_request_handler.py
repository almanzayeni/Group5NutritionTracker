'''
Created on Apr 8, 2026

@author: OpenAI
'''
import unittest

from model import database
from model.base_food import BaseFood
from model.composite_food import CompositeFood
from model.quantity_category import QuantityCategory
from server import add_food_request_handler
from server import constants


class TestAddFoodRequestHandler(unittest.TestCase):

    def setUp(self):
        self._original_food_items = database._foodItems
        database._foodItems = {}

    def tearDown(self):
        database._foodItems = self._original_food_items

    def test_returns_failure_when_food_item_missing(self):
        response = add_food_request_handler.handleRequest({})

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual("no food item", response[constants.KEY_FAILURE_MESSAGE])

    def test_adds_base_food_to_database(self):
        response = add_food_request_handler.handleRequest(
            self._create_base_food_request("greek yogurt", QuantityCategory.SERVING)
        )

        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertIn("greek yogurt", database.getFoodItems())

        added_food = database.getFoodItems()["greek yogurt"]
        self.assertIsInstance(added_food, BaseFood)
        self.assertEqual("greek yogurt", added_food.get_description())
        self.assertEqual(QuantityCategory.SERVING, added_food.get_quantity_category())
        self.assertEqual(1, added_food.get_portion_size())
        self.assertEqual(120, added_food.get_calories())
        self.assertEqual(15, added_food.get_protein())
        self.assertEqual(0, added_food.get_fat())
        self.assertEqual(7, added_food.get_sugar())
        self.assertEqual(9, added_food.get_carbohydrates())
        self.assertEqual(55, added_food.get_sodium())

    def test_adds_composite_food_to_database_when_all_ingredients_exist(self):
        banana = BaseFood("banana", QuantityCategory.QUANTITY, 1, 105, 1, 0, 14, 27, 1)
        oats = BaseFood("oats", QuantityCategory.SERVING, 1, 150, 5, 3, 1, 27, 0)
        database._foodItems = {
            banana.get_description(): banana,
            oats.get_description(): oats,
        }

        response = add_food_request_handler.handleRequest(
            self._create_composite_food_request("banana oatmeal", ["banana", "oats"])
        )

        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertIn("banana oatmeal", database.getFoodItems())

        added_food = database.getFoodItems()["banana oatmeal"]
        self.assertIsInstance(added_food, CompositeFood)
        self.assertEqual("banana oatmeal", added_food.get_description())
        self.assertEqual(QuantityCategory.SERVING, added_food.get_quantity_category())
        self.assertEqual(1, added_food.get_portion_size())
        self.assertEqual(255, added_food.get_calories())
        self.assertEqual(6, added_food.get_protein())
        self.assertEqual(3, added_food.get_fat())
        self.assertEqual(15, added_food.get_sugar())
        self.assertEqual(54, added_food.get_carbohydrates())
        self.assertEqual(1, added_food.get_sodium())
        self.assertEqual([banana, oats], added_food.get_ingredients())

    def test_returns_failure_when_composite_ingredient_missing_from_database(self):
        banana = BaseFood("banana", QuantityCategory.QUANTITY, 1, 105, 1, 0, 14, 27, 1)
        database._foodItems = {banana.get_description(): banana}

        response = add_food_request_handler.handleRequest(
            self._create_composite_food_request(
                "banana parfait",
                ["banana", "granola"],
            )
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "ingredient 'granola' not found in database",
            response[constants.KEY_FAILURE_MESSAGE],
        )
        self.assertNotIn("banana parfait", database.getFoodItems())

    def test_returns_failure_when_food_type_is_invalid(self):
        response = add_food_request_handler.handleRequest(
            {
                constants.KEY_FOOD_ITEM: {
                    constants.KEY_FOOD_TYPE: "invalid",
                }
            }
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual("invalid food type", response[constants.KEY_FAILURE_MESSAGE])

    def _create_base_food_request(self, description, quantity_category):
        return {
            constants.KEY_FOOD_ITEM: {
                constants.KEY_FOOD_TYPE: constants.KEY_BASE_FOOD_TYPE,
                constants.KEY_FOOD_DESCRIPTION: description,
                constants.KEY_FOOD_QUANTITY_CATEGORY: quantity_category,
                constants.KEY_FOOD_PORTION_SIZE: 1,
                constants.KEY_FOOD_CALORIES: 120,
                constants.KEY_FOOD_PROTEIN: 15,
                constants.KEY_FOOD_FAT: 0,
                constants.KEY_FOOD_SUGAR: 7,
                constants.KEY_FOOD_CARBS: 9,
                constants.KEY_FOOD_SODIUM: 55,
            }
        }

    def _create_composite_food_request(self, description, ingredients):
        return {
            constants.KEY_FOOD_ITEM: {
                constants.KEY_FOOD_TYPE: constants.KEY_COMPOSITE_FOOD_TYPE,
                constants.KEY_FOOD_DESCRIPTION: description,
                constants.KEY_FOOD_QUANTITY_CATEGORY: QuantityCategory.SERVING,
                constants.KEY_FOOD_PORTION_SIZE: 1,
                constants.KEY_FOOD_CALORIES: 255,
                constants.KEY_FOOD_PROTEIN: 6,
                constants.KEY_FOOD_FAT: 3,
                constants.KEY_FOOD_SUGAR: 15,
                constants.KEY_FOOD_CARBS: 54,
                constants.KEY_FOOD_SODIUM: 1,
                constants.KEY_INGREDIENTS: ingredients,
            }
        }


if __name__ == "__main__":
    unittest.main()
