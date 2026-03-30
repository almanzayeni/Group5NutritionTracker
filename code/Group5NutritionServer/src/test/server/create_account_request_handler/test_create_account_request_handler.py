'''
Created on Mar 30, 2026

@author: OpenAI
'''
from datetime import date as dateTime
import unittest

from model import database
from server import constants
from server import create_account_request_handler


class TestCreateAccountRequestHandler(unittest.TestCase):

    def setUp(self):
        self._original_users = database._users
        database._users = {}

    def tearDown(self):
        database._users = self._original_users

    def test_rejects_none_request(self):
        with self.assertRaisesRegex(Exception, "request is None"):
            create_account_request_handler.handleRequest(None)

    def test_requires_username(self):
        with self.assertRaisesRegex(Exception, "request does not contain username"):
            create_account_request_handler.handleRequest(
                {
                    constants.KEY_PASSWORD: "password123",
                    constants.KEY_NAME: "John Doe",
                }
            )

    def test_requires_password(self):
        with self.assertRaisesRegex(Exception, "request does not contain password"):
            create_account_request_handler.handleRequest(
                {
                    constants.KEY_USERNAME: "johndoe",
                    constants.KEY_NAME: "John Doe",
                }
            )

    def test_requires_name(self):
        with self.assertRaisesRegex(Exception, "request does not contain name"):
            create_account_request_handler.handleRequest(
                {
                    constants.KEY_USERNAME: "johndoe",
                    constants.KEY_PASSWORD: "password123",
                }
            )

    def test_returns_failure_when_username_already_exists(self):
        database._users["johndoe"] = {"password123": object()}

        response = create_account_request_handler.handleRequest(
            {
                constants.KEY_USERNAME: "johndoe",
                constants.KEY_PASSWORD: "password123",
                constants.KEY_NAME: "John Doe",
            }
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "username already exists",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_creates_new_user_with_default_values(self):
        response = create_account_request_handler.handleRequest(
            {
                constants.KEY_USERNAME: "janedoe",
                constants.KEY_PASSWORD: "secret",
                constants.KEY_NAME: "Jane Doe",
            }
        )

        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual("Jane Doe", response[constants.KEY_USER]["name"])
        self.assertEqual("janedoe", response[constants.KEY_USER]["username"])
        self.assertEqual("secret", response[constants.KEY_USER]["password"])
        self.assertEqual(dateTime.today().isoformat(), response[constants.KEY_USER]["currentFoodLog"]["date"])
        self.assertEqual([], response[constants.KEY_USER]["currentFoodLog"]["breakfast"])
        self.assertEqual("CALORIE", response[constants.KEY_USER]["dietGoals"]["primaryGoal"])
        self.assertIn("janedoe", database._users)
        self.assertIn("secret", database._users["janedoe"])


if __name__ == "__main__":
    unittest.main()
