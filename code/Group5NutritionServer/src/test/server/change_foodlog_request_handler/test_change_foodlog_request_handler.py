'''
Created on Apr 24, 2026

@author: OpenAI
'''
import unittest
from datetime import date
from unittest.mock import patch

from model.food_log import FoodLog
from server import change_foodlog_request_handler
from server import constants


class TestChangeFoodLogRequestHandler(unittest.TestCase):

    def _create_request(self, username="johndoe", password="password123", request_date="2026-04-21"):
        return {
            constants.KEY_USERNAME: username,
            constants.KEY_PASSWORD: password,
            constants.KEY_DATE: request_date,
        }

    def test_returns_failure_when_request_is_none(self):
        response = change_foodlog_request_handler.handelRequest(None)

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual("There was no request", response[constants.KEY_FAILURE_MESSAGE])

    def test_returns_failure_when_username_is_missing(self):
        response = change_foodlog_request_handler.handelRequest(
            {
                constants.KEY_PASSWORD: "password123",
                constants.KEY_DATE: "2026-04-21",
            }
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "request does not contain username",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_password_is_missing(self):
        response = change_foodlog_request_handler.handelRequest(
            {
                constants.KEY_USERNAME: "johndoe",
                constants.KEY_DATE: "2026-04-21",
            }
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "request does not contain password",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_date_is_missing(self):
        response = change_foodlog_request_handler.handelRequest(
            {
                constants.KEY_USERNAME: "johndoe",
                constants.KEY_PASSWORD: "password123",
            }
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "request does not contain date",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_date_does_not_have_three_parts(self):
        response = change_foodlog_request_handler.handelRequest(
            self._create_request(request_date="2026-04")
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "date is not in the correct format",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_date_contains_non_digits(self):
        response = change_foodlog_request_handler.handelRequest(
            self._create_request(request_date="2026-apr-21")
        )

        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            "date is not in the correct format",
            response[constants.KEY_FAILURE_MESSAGE],
        )

    def test_returns_failure_when_database_lookup_raises_exception(self):
        with patch.object(
            change_foodlog_request_handler.database,
            "searchFoodLogByDate",
            side_effect=Exception("incorrect password"),
        ) as search_food_log_by_date:
            response = change_foodlog_request_handler.handelRequest(
                self._create_request()
            )

        search_food_log_by_date.assert_called_once_with(
            "johndoe",
            "password123",
            date(2026, 4, 21),
        )
        self.assertEqual(constants.BAD_MESSAGE_STATUS, response[constants.KEY_STATUS])
        self.assertEqual("incorrect password", response[constants.KEY_FAILURE_MESSAGE])

    def test_returns_empty_food_log_when_database_has_no_food_log_for_date(self):
        with patch.object(
            change_foodlog_request_handler.database,
            "searchFoodLogByDate",
            return_value=None,
        ) as search_food_log_by_date:
            response = change_foodlog_request_handler.handelRequest(
                self._create_request()
            )

        search_food_log_by_date.assert_called_once_with(
            "johndoe",
            "password123",
            date(2026, 4, 21),
        )
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(
            {
                "date": "2026-04-21",
                "breakfast": [],
                "lunch": [],
                "dinner": [],
                "snacks": [],
            },
            response[constants.KEY_FOOD_LOG],
        )

    def test_returns_existing_food_log_when_database_finds_one(self):
        stored_food_log = FoodLog(date(2026, 4, 21), [], [], [], [])

        with patch.object(
            change_foodlog_request_handler.database,
            "searchFoodLogByDate",
            return_value=stored_food_log,
        ) as search_food_log_by_date:
            response = change_foodlog_request_handler.handelRequest(
                self._create_request()
            )

        search_food_log_by_date.assert_called_once_with(
            "johndoe",
            "password123",
            date(2026, 4, 21),
        )
        self.assertEqual(constants.SUCCESS_STATUS, response[constants.KEY_STATUS])
        self.assertEqual(stored_food_log.toDict(), response[constants.KEY_FOOD_LOG])


if __name__ == "__main__":
    unittest.main()
