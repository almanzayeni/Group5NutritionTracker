'''
Created on Mar 30, 2026

@author: OpenAI
'''
import unittest
from datetime import date

from model.user import User


class _FakeFoodLog(object):

    def __init__(self, log_date, serialized):
        self._log_date = log_date
        self._serialized = serialized

    def getDate(self):
        return self._log_date

    def toDict(self):
        return self._serialized


class _FakeDietGoals(object):

    def __init__(self, serialized):
        self._serialized = serialized

    def toDict(self):
        return self._serialized


class TestUser(unittest.TestCase):

    def _create_user(self, **overrides):
        values = {
            "name": "John Doe",
            "username": "johndoe",
            "password": "password123",
            "currentFoodLog": _FakeFoodLog(
                date(2026, 3, 30),
                {"date": "2026-03-30"},
            ),
            "dietGoals": _FakeDietGoals({"primaryGoal": "CALORIE"}),
        }
        values.update(overrides)
        return User(**values)

    def test_constructor_sets_all_values(self):
        food_log = _FakeFoodLog(date(2026, 3, 30), {"date": "2026-03-30"})
        diet_goals = _FakeDietGoals({"primaryGoal": "CALORIE"})

        user = self._create_user(currentFoodLog=food_log, dietGoals=diet_goals)

        self.assertEqual("John Doe", user.getName())
        self.assertEqual("johndoe", user.getUsername())
        self.assertEqual("password123", user.getPassword())
        self.assertIs(food_log, user.getCurrentFoodLog())
        self.assertIs(diet_goals, user.getDietGoals())
        self.assertEqual({date(2026, 3, 30): food_log}, user.getStoredFoodLogs())

    def test_constructor_validates_invalid_arguments(self):
        cases = [
            ({"name": None}, "name is None"),
            ({"username": None}, "username is None"),
            ({"password": None}, "password is None"),
            ({"currentFoodLog": None}, "food log is None"),
            ({"dietGoals": None}, "diet goals is None"),
        ]

        for overrides, expected_message in cases:
            with self.subTest(overrides=overrides):
                with self.assertRaisesRegex(Exception, expected_message):
                    self._create_user(**overrides)

    def test_to_dict_returns_nested_serialized_values(self):
        user = self._create_user(
            currentFoodLog=_FakeFoodLog(date(2026, 3, 30), {"date": "2026-03-30"}),
            dietGoals=_FakeDietGoals({"primaryGoal": "CALORIE"}),
        )

        self.assertEqual(
            {
                "name": "John Doe",
                "username": "johndoe",
                "password": "password123",
                "currentFoodLog": {"date": "2026-03-30"},
                "dietGoals": {"primaryGoal": "CALORIE"},
            },
            user.toDict(),
        )

    def test_set_current_food_log_rejects_none(self):
        user = self._create_user()

        with self.assertRaisesRegex(Exception, "food log is None"):
            user.setCurrentFoodLog(None)

    def test_add_food_log_rejects_none(self):
        user = self._create_user()

        with self.assertRaisesRegex(Exception, "food log is None"):
            user.addFoodLog(None)

    def test_set_diet_goals_rejects_none(self):
        user = self._create_user()

        with self.assertRaisesRegex(Exception, "diet goals is None"):
            user.setDietGoals(None)

    def test_set_diet_goals_updates_diet_goals(self):
        user = self._create_user()
        updated_diet_goals = _FakeDietGoals({"primaryGoal": "PROTEIN"})

        user.setDietGoals(updated_diet_goals)

        self.assertIs(updated_diet_goals, user.getDietGoals())


if __name__ == "__main__":
    unittest.main()
