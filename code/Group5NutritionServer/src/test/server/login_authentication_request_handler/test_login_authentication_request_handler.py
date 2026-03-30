'''
Created on Mar 30, 2026

@author: OpenAI
'''
from datetime import date as dateTime
import unittest

from model import database
from model.diet_goals import DietGoals
from model.food_log import FoodLog
from model.user import User
from server import constants
from server import login_authentication_request_handler


class TestLoginAuthenticationRequestHandler(unittest.TestCase):

    def setUp(self):
        self._original_users = database._users
        database._users = {}

    def tearDown(self):
        database._users = self._original_users

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
        user = User(
            "John Doe",
            "johndoe",
            "password123",
            FoodLog(dateTime.today(), [], [], [], []),
            DietGoals("CALORIE", 2000, 150, 70, 50, 2300, 250, []),
        )
        database._users["johndoe"] = {"password123": user}

        response = login_authentication_request_handler.handleRequest(
            {
                constants.KEY_USERNAME: "johndoe",
                constants.KEY_PASSWORD: "password123",
            }
        )

        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(user.toDict(), response[constants.KEY_USER])


if __name__ == "__main__":
    unittest.main()
