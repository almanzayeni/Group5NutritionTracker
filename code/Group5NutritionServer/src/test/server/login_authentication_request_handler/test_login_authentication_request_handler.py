'''
Created on Mar 30, 2026

@author: OpenAI
'''
from datetime import date as dateTime
from datetime import timedelta
import unittest
from unittest.mock import patch

from model import database
from model.base_food import BaseFood
from model.diet_goals import DietGoals
from model.food_log import FoodLog
from model.quantity_category import QuantityCategory
from model.user import User
from server import constants
from server import login_authentication_request_handler


class TestLoginAuthenticationRequestHandler(unittest.TestCase):

    def setUp(self):
        self._original_users = database._users
        database._users = {}

    def tearDown(self):
        database._users = self._original_users

    def _create_user(self, food_log):
        return User(
            "John Doe",
            "johndoe",
            "password123",
            food_log,
            DietGoals("CALORIE", 2000, 150, 70, 50, 2300, 250, []),
        )

    def test_rejects_none_request(self):
        with self.assertRaisesRegex(Exception, "request is None"):
            login_authentication_request_handler.handleRequest(None)

    def test_requires_username(self):
        with self.assertRaisesRegex(Exception, "request does not contain username"):
            login_authentication_request_handler.handleRequest(
                {constants.KEY_PASSWORD: "password123"}
            )

    def test_requires_password(self):
        with self.assertRaisesRegex(Exception, "request does not contain password"):
            login_authentication_request_handler.handleRequest(
                {constants.KEY_USERNAME: "johndoe"}
            )

    def test_returns_failure_for_invalid_credentials(self):
        response = login_authentication_request_handler.handleRequest(
            {
                constants.KEY_USERNAME: "unknown",
                constants.KEY_PASSWORD: "wrong",
            }
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "invalid username or password",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_user_for_valid_credentials(self):
        user = self._create_user(FoodLog(dateTime.today(), [], [], [], []))
        database._users["johndoe"] = {"password123": user}

        response = login_authentication_request_handler.handleRequest(
            {
                constants.KEY_USERNAME: "johndoe",
                constants.KEY_PASSWORD: "password123",
            }
        )

        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(user.toDict(), response[constants.KEY_USER])

    def test_refreshes_current_food_log_from_database_when_stored_log_is_outdated(self):
        old_food_log = FoodLog(dateTime.today() - timedelta(days=1), [], [], [], [])
        today_food = BaseFood(
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
        today_food_log = FoodLog(dateTime.today(), [today_food], [], [], [])
        user = self._create_user(old_food_log)
        database._users["johndoe"] = {"password123": user}

        with patch.object(
            login_authentication_request_handler.database,
            "searchFoodLogByDate",
            return_value=today_food_log,
        ) as search_food_log_by_date, patch.object(
            login_authentication_request_handler.database,
            "addFoodLog",
        ) as add_food_log:
            response = login_authentication_request_handler.handleRequest(
                {
                    constants.KEY_USERNAME: "johndoe",
                    constants.KEY_PASSWORD: "password123",
                }
            )

        search_food_log_by_date.assert_called_once_with("johndoe", dateTime.today())
        add_food_log.assert_not_called()
        self.assertEqual(today_food_log, user.getCurrentFoodLog())
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(user.toDict(), response[constants.KEY_USER])

    def test_creates_empty_food_log_when_no_food_log_exists_for_today(self):
        old_food_log = FoodLog(dateTime.today() - timedelta(days=1), [], [], [], [])
        user = self._create_user(old_food_log)
        database._users["johndoe"] = {"password123": user}

        with patch.object(
            login_authentication_request_handler.database,
            "searchFoodLogByDate",
            return_value=None,
        ) as search_food_log_by_date, patch.object(
            login_authentication_request_handler.database,
            "addFoodLog",
        ) as add_food_log:
            response = login_authentication_request_handler.handleRequest(
                {
                    constants.KEY_USERNAME: "johndoe",
                    constants.KEY_PASSWORD: "password123",
                }
            )

        search_food_log_by_date.assert_called_once_with("johndoe", dateTime.today())
        add_food_log.assert_called_once_with("johndoe", user.getCurrentFoodLog())
        self.assertEqual(dateTime.today(), user.getCurrentFoodLog().getDate())
        self.assertEqual([], user.getCurrentFoodLog().getBreakfast())
        self.assertEqual([], user.getCurrentFoodLog().getLunch())
        self.assertEqual([], user.getCurrentFoodLog().getDinner())
        self.assertEqual([], user.getCurrentFoodLog().getSnacks())
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(user.toDict(), response[constants.KEY_USER])


if __name__ == "__main__":
    unittest.main()
