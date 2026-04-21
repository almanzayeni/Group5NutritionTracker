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
        self._original_food_logs = database._foodLogs

        database._users = {}
        database._foodItems = {}
        database._foodLogs = {}

    def tearDown(self):
        database._users = self._original_users
        database._foodItems = self._original_food_items
        database._foodLogs = self._original_food_logs

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
        food_logs = database.getFoodLogs()

        self.assertIn("johndoe", users)
        self.assertIn("password123", users["johndoe"])
        self.assertEqual("John Doe", users["johndoe"]["password123"].getName())
        self.assertEqual(14, len(food_items))
        self.assertIn("oatmeal", food_items)
        self.assertIn("hamburger", food_items)
        self.assertIn("johndoe", food_logs)
        self.assertEqual(2, len(food_logs["johndoe"]))
        self.assertIn(date(2026, 4, 19), food_logs["johndoe"])

    def test_getters_return_backing_storage(self):
        self.assertIs(database._users, database.getUsers())
        self.assertIs(database._foodItems, database.getFoodItems())
        self.assertIs(database._foodLogs, database.getFoodLogs())

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
            database.addFoodLog(None, self._create_food_log(date(2026, 4, 20)))

    def test_add_food_log_rejects_none_food_log(self):
        with self.assertRaisesRegex(Exception, "food log is None"):
            database.addFoodLog("johndoe", None)

    def test_add_food_log_creates_bucket_for_new_user(self):
        food_log = self._create_food_log(date(2026, 4, 20))

        database.addFoodLog("johndoe", food_log)

        self.assertEqual(food_log, database.getFoodLogs()["johndoe"][date(2026, 4, 20)])

    def test_add_food_log_adds_to_existing_user_bucket(self):
        existing_food_log = self._create_food_log(date(2026, 4, 20))
        new_food_log = self._create_food_log(date(2026, 4, 21))
        database._foodLogs = {"johndoe": {date(2026, 4, 20): existing_food_log}}

        database.addFoodLog("johndoe", new_food_log)

        self.assertEqual(existing_food_log, database.getFoodLogs()["johndoe"][date(2026, 4, 20)])
        self.assertEqual(new_food_log, database.getFoodLogs()["johndoe"][date(2026, 4, 21)])

    def test_update_food_log_rejects_none_username(self):
        with self.assertRaisesRegex(Exception, "username is None"):
            database.updateFoodLog(None, self._create_food_log(date(2026, 4, 20)))

    def test_update_food_log_rejects_none_food_log(self):
        with self.assertRaisesRegex(Exception, "food log is None"):
            database.updateFoodLog("johndoe", None)

    def test_update_food_log_rejects_unknown_user(self):
        with self.assertRaisesRegex(Exception, "user not found"):
            database.updateFoodLog(
                "missing-user",
                self._create_food_log(date(2026, 4, 20)),
            )

    def test_update_food_log_adds_missing_date_for_existing_user(self):
        existing_food_log = self._create_food_log(date(2026, 4, 20))
        new_food_log = self._create_food_log(date(2026, 4, 21))
        database._foodLogs = {"johndoe": {date(2026, 4, 20): existing_food_log}}

        database.updateFoodLog("johndoe", new_food_log)

        self.assertEqual(existing_food_log, database.getFoodLogs()["johndoe"][date(2026, 4, 20)])
        self.assertEqual(new_food_log, database.getFoodLogs()["johndoe"][date(2026, 4, 21)])

    def test_update_food_log_replaces_existing_log(self):
        original_food_log = self._create_food_log(date(2026, 4, 20))
        updated_food = self._create_food("banana")
        updated_food_log = FoodLog(date(2026, 4, 20), [updated_food], [], [], [])
        database._foodLogs = {"johndoe": {date(2026, 4, 20): original_food_log}}

        database.updateFoodLog("johndoe", updated_food_log)

        self.assertEqual(updated_food_log, database.getFoodLogs()["johndoe"][date(2026, 4, 20)])

    def test_search_food_item_by_description_rejects_none_query(self):
        with self.assertRaisesRegex(Exception, "query is None"):
            database.searchFoodItemByDescription(None)

    def test_search_food_item_by_description_returns_empty_list_for_empty_query(self):
        self.assertEqual([], database.searchFoodItemByDescription(""))

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
            database.searchFoodLogByDate(None, date(2026, 4, 20))

    def test_search_food_log_by_date_rejects_none_date(self):
        with self.assertRaisesRegex(Exception, "date is None"):
            database.searchFoodLogByDate("johndoe", None)

    def test_search_food_log_by_date_rejects_unknown_user(self):
        with self.assertRaisesRegex(Exception, "user not found"):
            database.searchFoodLogByDate("missing-user", date(2026, 4, 20))

    def test_search_food_log_by_date_returns_none_when_date_missing(self):
        database._foodLogs = {"johndoe": {date(2026, 4, 20): self._create_food_log(date(2026, 4, 20))}}

        self.assertIsNone(database.searchFoodLogByDate("johndoe", date(2026, 4, 21)))

    def test_search_food_log_by_date_returns_food_log_when_present(self):
        food_log = self._create_food_log(date(2026, 4, 20))
        database._foodLogs = {"johndoe": {date(2026, 4, 20): food_log}}

        self.assertEqual(food_log, database.searchFoodLogByDate("johndoe", date(2026, 4, 20)))


if __name__ == "__main__":
    unittest.main()
