'''
Created on Apr 24, 2026

@author: OpenAI
'''
import unittest
from datetime import date
from unittest.mock import patch

from model import database
from model.diet_goals import DietGoals
from model.food_log import FoodLog
from model.user import User
from server import constants
from server import edit_diet_goals_request_handler


class TestEditDietGoalsRequestHandler(unittest.TestCase):

    def setUp(self):
        self._original_users = database._users
        database._users = {}

    def tearDown(self):
        database._users = self._original_users

    def _create_user(self, username="johndoe", password="password123"):
        return User(
            "John Doe",
            username,
            password,
            FoodLog(date(2026, 4, 20), [], [], [], []),
            DietGoals("CALORIE", 2000, 150, 70, 50, 2300, 250, []),
        )

    def _create_request(self, username="johndoe", password="password123", diet_goals=None):
        if diet_goals is None:
            diet_goals = {
                constants.KEY_PRIMARY_GOAL: "PROTEIN",
                constants.KEY_CALORIE_GOAL: 1800,
                constants.KEY_PROTEIN_GOAL: 160,
                constants.KEY_FAT_GOAL: 60,
                constants.KEY_SUGAR_GOAL: 40,
                constants.KEY_SODIUM_GOAL: 2000,
                constants.KEY_CARBS_GOAL: 190,
                constants.KEY_OTHER_GOALS: ["eat more fiber"],
            }

        return {
            constants.KEY_USERNAME: username,
            constants.KEY_PASSWORD: password,
            constants.KEY_DIET_GOALS: diet_goals,
        }

    def test_returns_failure_when_request_is_none(self):
        response = edit_diet_goals_request_handler.handleRequest(None)

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual("request is None", response[constants.KEY_FAILURE_MESSAGE])

    def test_returns_failure_when_username_missing(self):
        response = edit_diet_goals_request_handler.handleRequest(
            {
                constants.KEY_PASSWORD: "password123",
                constants.KEY_DIET_GOALS: {},
            }
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "request does not contain username",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_password_missing(self):
        response = edit_diet_goals_request_handler.handleRequest(
            {
                constants.KEY_USERNAME: "johndoe",
                constants.KEY_DIET_GOALS: {},
            }
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "request does not contain password",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_diet_goals_missing(self):
        response = edit_diet_goals_request_handler.handleRequest(
            {
                constants.KEY_USERNAME: "johndoe",
                constants.KEY_PASSWORD: "password123",
            }
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "request does not contain diet goals",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_diet_goals_are_invalid(self):
        response = edit_diet_goals_request_handler.handleRequest(
            self._create_request(
                diet_goals={
                    constants.KEY_PRIMARY_GOAL: None,
                    constants.KEY_CALORIE_GOAL: 1800,
                    constants.KEY_PROTEIN_GOAL: 160,
                    constants.KEY_FAT_GOAL: 60,
                    constants.KEY_SUGAR_GOAL: 40,
                    constants.KEY_SODIUM_GOAL: 2000,
                    constants.KEY_CARBS_GOAL: 190,
                    constants.KEY_OTHER_GOALS: [],
                }
            )
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "invalid diet goals: primary goal is None",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_for_invalid_credentials(self):
        response = edit_diet_goals_request_handler.handleRequest(self._create_request())

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "invalid username or password",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_username_or_password_value_is_none(self):
        response = edit_diet_goals_request_handler.handleRequest(
            self._create_request(username=None)
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "Username or Password is None",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_updates_user_diet_goals_when_request_is_valid(self):
        user = self._create_user()
        database._users = {user.getUsername(): {user.getPassword(): user}}

        response = edit_diet_goals_request_handler.handleRequest(self._create_request())

        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        updated_diet_goals = database.getUsers()["johndoe"]["password123"].getDietGoals()
        self.assertEqual("PROTEIN", updated_diet_goals.getPrimaryGoal())
        self.assertEqual(1800, updated_diet_goals.getCalorieGoal())
        self.assertEqual(160, updated_diet_goals.getProteinGoal())
        self.assertEqual(60, updated_diet_goals.getFatGoal())
        self.assertEqual(40, updated_diet_goals.getSugarGoal())
        self.assertEqual(2000, updated_diet_goals.getSodiumGoal())
        self.assertEqual(190, updated_diet_goals.getCarbsGoal())
        self.assertEqual(["eat more fiber"], updated_diet_goals.getOtherGoals())

    def test_returns_failure_when_database_lookup_raises_non_key_error(self):
        with patch.object(
            edit_diet_goals_request_handler.database,
            "getUser",
            side_effect=Exception("username is None"),
        ):
            response = edit_diet_goals_request_handler.handleRequest(
                self._create_request()
            )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "Username or Password is None",
            response[constants.KEY_FAILURE_MESSAGE],
        )


if __name__ == "__main__":
    unittest.main()
