'''
Created on Mar 30, 2026

@author: OpenAI
'''
import unittest

from model import database
from model.base_food import BaseFood
from model.quantity_category import QuantityCategory


class TestDatabase(unittest.TestCase):

    def setUp(self):
        self._original_users = database._users
        self._original_food_items = database._foodItems
        database._users = {}
        database._foodItems = {}

    def tearDown(self):
        database._users = self._original_users
        database._foodItems = self._original_food_items

    def test_load_default_data_populates_default_user_and_food_items(self):
        database.loadDefaultData()

        users = database.getUsers()
        food_items = database.getFoodItems()

        self.assertIn("johndoe", users)
        self.assertIn("password123", users["johndoe"])
        self.assertEqual("John Doe", users["johndoe"]["password123"].getName())
        self.assertEqual(14, len(food_items))
        self.assertIn("oatmeal", food_items)
        self.assertIn("hamburger", food_items)

    def test_getters_return_backing_storage(self):
        self.assertIs(database._users, database.getUsers())
        self.assertIs(database._foodItems, database.getFoodItems())

    def test_add_user_rejects_none(self):
        with self.assertRaisesRegex(Exception, "user is None"):
            database.addUser(None)

    def test_add_user_raises_attribute_error_for_non_none_user_with_current_storage_shape(self):
        with self.assertRaises(AttributeError):
            database.addUser(object())

    def test_add_food_item_rejects_none(self):
        with self.assertRaisesRegex(Exception, "food item is None"):
            database.addFoodItem(None)

    def test_add_food_item_stores_food_by_description(self):
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

        database.addFoodItem(food)

        self.assertEqual(food, database.getFoodItems()["banana"])

    def test_search_food_item_by_description_rejects_none_query(self):
        with self.assertRaisesRegex(Exception, "query is None"):
            database.searchFoodItemByDescription(None)

    def test_search_food_item_by_description_returns_empty_list_for_empty_query(self):
        self.assertEqual([], database.searchFoodItemByDescription(""))

    def test_search_food_item_by_description_matches_case_insensitively(self):
        banana = BaseFood(
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
        apple = BaseFood(
            "apple",
            QuantityCategory.QUANTITY,
            1,
            95,
            0,
            0,
            19,
            25,
            2,
        )
        database._foodItems = {
            "Banana": banana,
            "apple": apple,
        }

        self.assertEqual([banana], database.searchFoodItemByDescription("nAn"))


if __name__ == "__main__":
    unittest.main()
