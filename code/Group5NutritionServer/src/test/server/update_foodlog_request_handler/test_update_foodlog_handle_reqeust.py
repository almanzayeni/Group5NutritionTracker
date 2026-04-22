'''
Created on April 21, 2026

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
from server import constants
from server import update_foodlog_request_handler


class TestUpdateFoodLogHandleRequest(unittest.TestCase):

    def setUp(self):
        self._original_food_items = database._foodItems
        self._original_users = database._users

        self.oatmeal = self._create_food("oatmeal")
        self.salad = self._create_food("salad")
        self.soup = self._create_food("soup")
        self.apple = self._create_food("apple")

        database._foodItems = {
            self.oatmeal.get_description(): self.oatmeal,
            self.salad.get_description(): self.salad,
            self.soup.get_description(): self.soup,
            self.apple.get_description(): self.apple,
        }

        current_food_log = FoodLog(
            date(2026, 4, 20),
            [self.apple],
            [],
            [],
            [],
        )
        diet_goals = DietGoals("CALORIE", 2000, 150, 70, 50, 2300, 250, [])
        user = User("John Doe", "johndoe", "password123", current_food_log, diet_goals)
        database._users = {user.getUsername(): {user.getPassword(): user}}

    def tearDown(self):
        database._foodItems = self._original_food_items
        database._users = self._original_users

    def _create_food(self, description):
        return BaseFood(
            description,
            QuantityCategory.SERVING,
            1,
            100,
            10,
            2,
            1,
            15,
            50,
        )

    def _create_request(
        self,
        breakfast=None,
        lunch=None,
        dinner=None,
        snacks=None,
    ):
        return {
            constants.KEY_USERNAME: "johndoe",
            constants.KEY_PASSWORD: "password123",
            constants.KEY_FOOD_LOG: {
                constants.KEY_DATE: "2026-04-20",
                constants.KEY_BREAKFAST: breakfast or [],
                constants.KEY_LUNCH: lunch or [],
                constants.KEY_DINNER: dinner or [],
                constants.KEY_SNACKS: snacks or [],
            },
        }

    def _create_food_entry(self, description):
        return {constants.KEY_FOOD_DESCRIPTION: description}

    def test_rejects_none_request(self):
        with self.assertRaisesRegex(Exception, "request is None"):
            update_foodlog_request_handler.handleRequest(None)

    def test_requires_username(self):
        with self.assertRaisesRegex(Exception, "request does not contain username"):
            update_foodlog_request_handler.handleRequest({})

    def test_requires_password(self):
        with self.assertRaisesRegex(Exception, "request does not contain password"):
            update_foodlog_request_handler.handleRequest(
                {constants.KEY_USERNAME: "johndoe"}
            )

    def test_requires_food_log(self):
        with self.assertRaisesRegex(Exception, "request does not contain food log"):
            update_foodlog_request_handler.handleRequest(
                {
                    constants.KEY_USERNAME: "johndoe",
                    constants.KEY_PASSWORD: "password123",
                }
            )

    def test_returns_failure_when_breakfast_item_is_not_found(self):
        response = update_foodlog_request_handler.handleRequest(
            self._create_request(
                breakfast=[self._create_food_entry("missing breakfast")]
            )
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "food item 'missing breakfast' not found in database",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_lunch_item_is_not_found(self):
        response = update_foodlog_request_handler.handleRequest(
            self._create_request(lunch=[self._create_food_entry("missing lunch")])
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "food item 'missing lunch' not found in database",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_dinner_item_is_not_found(self):
        response = update_foodlog_request_handler.handleRequest(
            self._create_request(dinner=[self._create_food_entry("missing dinner")])
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "food item 'missing dinner' not found in database",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_snack_item_is_not_found(self):
        response = update_foodlog_request_handler.handleRequest(
            self._create_request(snacks=[self._create_food_entry("missing snack")])
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "food item 'missing snack' not found in database",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_updates_food_log_when_all_items_are_found(self):
        response = update_foodlog_request_handler.handleRequest(
            self._create_request(
                breakfast=[self._create_food_entry("oatmeal")],
                lunch=[self._create_food_entry("salad")],
                dinner=[self._create_food_entry("soup")],
                snacks=[self._create_food_entry("apple")],
            )
        )

        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])

        user = database.getUsers()["johndoe"]["password123"]
        updated_food_log = user.getStoredFoodLogs()[date(2026, 4, 20)]
        self.assertEqual([self.oatmeal], updated_food_log.getBreakfast())
        self.assertEqual([self.salad], updated_food_log.getLunch())
        self.assertEqual([self.soup], updated_food_log.getDinner())
        self.assertEqual([self.apple], updated_food_log.getSnacks())
        self.assertIs(updated_food_log, user.getCurrentFoodLog())


if __name__ == "__main__":
    unittest.main()
