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

    def test_requires_user(self):
        with self.assertRaisesRegex(Exception, "request does not contain user."):
            create_account_request_handler.handleRequest({})

    def test_requires_username(self):
        with self.assertRaisesRegex(Exception, "request does not contain username"):
            create_account_request_handler.handleRequest(
                self._create_request_without(constants.KEY_USERNAME)
            )

    def test_requires_password(self):
        with self.assertRaisesRegex(Exception, "request does not contain password"):
            create_account_request_handler.handleRequest(
                self._create_request_without(constants.KEY_PASSWORD)
            )

    def test_requires_name(self):
        with self.assertRaisesRegex(Exception, "request does not contain name"):
            create_account_request_handler.handleRequest(
                self._create_request_without(constants.KEY_NAME)
            )

    def test_returns_failure_when_username_already_exists(self):
        database._users["johndoe"] = {"password123": object()}

        response = create_account_request_handler.handleRequest(
            self._create_valid_request()
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "username already exists",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_creates_new_user_with_default_values(self):
        response = create_account_request_handler.handleRequest(
            self._create_valid_request(
                username="janedoe",
                password="secret",
                name="Jane Doe",
                other_goals=["Eat more vegetables"],
            )
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

    def _create_valid_request(
        self,
        username="johndoe",
        password="password123",
        name="John Doe",
        primary_goal="CALORIE",
        calorie_goal=2000,
        protein_goal=150,
        fat_goal=70,
        sugar_goal=50,
        sodium_goal=2300,
        carbs_goal=250,
        other_goals=None,
    ):
        if other_goals is None:
            other_goals = []

        return {
            constants.KEY_USER: {
                constants.KEY_USERNAME: username,
                constants.KEY_PASSWORD: password,
                constants.KEY_NAME: name,
                constants.KEY_DIET_GOALS: {
                    constants.KEY_PRIMARY_GOAL: primary_goal,
                    constants.KEY_CALORIE_GOAL: calorie_goal,
                    constants.KEY_PROTEIN_GOAL: protein_goal,
                    constants.KEY_FAT_GOAL: fat_goal,
                    constants.KEY_SUGAR_GOAL: sugar_goal,
                    constants.KEY_SODIUM_GOAL: sodium_goal,
                    constants.KEY_CARBS_GOAL: carbs_goal,
                    constants.KEY_OTHER_GOALS: other_goals,
                },
            }
        }

    def _create_request_without(self, key):
        request = self._create_valid_request()
        del request[constants.KEY_USER][key]
        return request


if __name__ == "__main__":
    unittest.main()
