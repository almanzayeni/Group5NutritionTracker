'''
Created on Apr 21, 2026

@author: OpenAI
'''
import unittest
from datetime import date

from model import database
from model.base_food import BaseFood
from model.diet_goals import DietGoals
from model.food_log import FoodLog
from model.quantity_category import QuantityCategory
from model.user import User


class TestDatabase(unittest.TestCase):

    def setUp(self):
        self._original_users = database._users
        self._original_food_items = database._foodItems

        database._users = {}
        database._foodItems = {}

    def tearDown(self):
        database._users = self._original_users
        database._foodItems = self._original_food_items

    def _create_food(self, description):
        return BaseFood(
            description,
            QuantityCategory.QUANTITY,
            1,
            105,
            1,
            0,
            14,
            27,
            1,
        )

    def _create_food_log(self, log_date):
        return FoodLog(log_date, [], [], [], [])

    def _create_user(self, username="johndoe", password="password123"):
        food_log = self._create_food_log(date(2026, 4, 20))
        diet_goals = DietGoals("CALORIE", 2000, 150, 70, 50, 2300, 250, [])
        return User("John Doe", username, password, food_log, diet_goals)

    def test_load_default_data_populates_default_user_food_items_and_food_logs(self):
        database.loadDefaultData()

        users = database.getUsers()
        food_items = database.getFoodItems()
        user = users["johndoe"]["password123"]
        stored_food_logs = user.getStoredFoodLogs()

        self.assertIn("johndoe", users)
        self.assertIn("password123", users["johndoe"])
        self.assertEqual("John Doe", user.getName())
        self.assertIn("oatmeal", food_items)
        self.assertIn("hamburger", food_items)
        self.assertIn("pretzels", food_items)
        self.assertIn(date(2026, 4, 19), stored_food_logs)
        self.assertIn(date(2026, 4, 20), stored_food_logs)
        self.assertIn(date.today(), stored_food_logs)

    def test_getters_return_backing_storage(self):
        self.assertIs(database._users, database.getUsers())
        self.assertIs(database._foodItems, database.getFoodItems())

    def test_add_user_rejects_none(self):
        with self.assertRaisesRegex(Exception, "user is None"):
            database.addUser(None)

    def test_add_user_stores_user_by_username_and_password(self):
        user = self._create_user()

        database.addUser(user)

        self.assertEqual(
            user,
            database.getUsers()[user.getUsername()][user.getPassword()],
        )

    def test_add_food_item_rejects_none(self):
        with self.assertRaisesRegex(Exception, "food item is None"):
            database.addFoodItem(None)

    def test_add_food_item_stores_food_by_description(self):
        banana = self._create_food("banana")

        database.addFoodItem(banana)

        self.assertEqual(banana, database.getFoodItems()["banana"])

    def test_add_food_log_rejects_none_username(self):
        with self.assertRaisesRegex(Exception, "username is None"):
            database.addFoodLog(None, "password123", self._create_food_log(date(2026, 4, 20)))

    def test_add_food_log_rejects_none_password(self):
        with self.assertRaisesRegex(Exception, "password is None"):
            database.addFoodLog("johndoe", None, self._create_food_log(date(2026, 4, 20)))

    def test_add_food_log_rejects_none_food_log(self):
        with self.assertRaisesRegex(Exception, "food log is None"):
            database.addFoodLog("johndoe", "password123", None)

    def test_add_food_log_rejects_unknown_user(self):
        with self.assertRaisesRegex(Exception, "user not found"):
            database.addFoodLog(
                "missing-user",
                "password123",
                self._create_food_log(date(2026, 4, 20)),
            )

    def test_add_food_log_rejects_incorrect_password(self):
        database.addUser(self._create_user())

        with self.assertRaisesRegex(Exception, "incorrect password"):
            database.addFoodLog(
                "johndoe",
                "wrong-password",
                self._create_food_log(date(2026, 4, 21)),
            )

    def test_add_food_log_adds_to_existing_user_bucket(self):
        user = self._create_user()
        new_food_log = self._create_food_log(date(2026, 4, 21))
        database.addUser(user)

        database.addFoodLog("johndoe", "password123", new_food_log)

        self.assertEqual(
            new_food_log,
            user.getStoredFoodLogs()[date(2026, 4, 21)],
        )

    def test_search_food_item_by_description_rejects_none_query(self):
        with self.assertRaisesRegex(Exception, "query is None"):
            database.searchFoodItemByDescription(None)

    def test_search_food_item_by_description_returns_all_food_items_for_empty_query(self):
        banana = self._create_food("Banana")
        apple = self._create_food("apple")
        database._foodItems = {
            "Banana": banana,
            "apple": apple,
        }

        self.assertCountEqual(
            [banana, apple],
            list(database.searchFoodItemByDescription("")),
        )

    def test_search_food_item_by_description_matches_case_insensitively(self):
        banana = self._create_food("Banana")
        apple = self._create_food("apple")
        database._foodItems = {
            "Banana": banana,
            "apple": apple,
        }

        self.assertEqual([banana], database.searchFoodItemByDescription("nAn"))

    def test_search_food_log_by_date_rejects_none_username(self):
        with self.assertRaisesRegex(Exception, "username is None"):
            database.searchFoodLogByDate(None, "password123", date(2026, 4, 20))

    def test_search_food_log_by_date_rejects_none_password(self):
        with self.assertRaisesRegex(Exception, "password is None"):
            database.searchFoodLogByDate("johndoe", None, date(2026, 4, 20))

    def test_search_food_log_by_date_rejects_none_date(self):
        with self.assertRaisesRegex(Exception, "date is None"):
            database.searchFoodLogByDate("johndoe", "password123", None)

    def test_search_food_log_by_date_rejects_unknown_user(self):
        with self.assertRaisesRegex(Exception, "user not found"):
            database.searchFoodLogByDate("missing-user", "password123", date(2026, 4, 20))

    def test_search_food_log_by_date_rejects_incorrect_password(self):
        database.addUser(self._create_user())

        with self.assertRaisesRegex(Exception, "incorrect password"):
            database.searchFoodLogByDate("johndoe", "wrong-password", date(2026, 4, 20))

    def test_search_food_log_by_date_returns_none_when_date_missing(self):
        database.addUser(self._create_user())

        self.assertIsNone(
            database.searchFoodLogByDate("johndoe", "password123", date(2026, 4, 21))
        )

    def test_search_food_log_by_date_returns_food_log_when_present(self):
        user = self._create_user()
        database.addUser(user)

        self.assertEqual(
            user.getCurrentFoodLog(),
            database.searchFoodLogByDate("johndoe", "password123", date(2026, 4, 20)),
        )


if __name__ == "__main__":
    unittest.main()
