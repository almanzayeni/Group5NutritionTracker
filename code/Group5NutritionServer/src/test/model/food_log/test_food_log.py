'''
Created on Mar 30, 2026

@author: OpenAI
'''
from datetime import date as dateTime
from datetime import timedelta
import unittest

from model.food_log import FoodLog


class _FakeFood(object):

    def __init__(self, serialized):
        self._serialized = serialized

    def to_dict(self):
        return self._serialized


class TestFoodLog(unittest.TestCase):

    def _create_food_log(self, **overrides):
        values = {
            "date": dateTime.today(),
            "breakfast": [],
            "lunch": [],
            "dinner": [],
            "snacks": [],
        }
        values.update(overrides)
        return FoodLog(**values)

    def test_constructor_sets_all_values(self):
        today = dateTime.today()
        breakfast = ["oatmeal"]
        lunch = ["salad"]
        dinner = ["salmon"]
        snacks = ["apple"]

        food_log = self._create_food_log(
            date=today,
            breakfast=breakfast,
            lunch=lunch,
            dinner=dinner,
            snacks=snacks,
        )

        self.assertEqual(today, food_log.getDate())
        self.assertEqual(breakfast, food_log.getBreakfast())
        self.assertEqual(lunch, food_log.getLunch())
        self.assertEqual(dinner, food_log.getDinner())
        self.assertEqual(snacks, food_log.getSnacks())

    def test_constructor_validates_invalid_arguments(self):
        cases = [
            ({"date": None}, "date is None"),
            ({"date": dateTime.today() + timedelta(days=1)}, "date is in the future"),
            ({"breakfast": None}, "breakfast is None"),
            ({"breakfast": [None]}, "breakfast contains None"),
            ({"lunch": None}, "lunch is None"),
            ({"lunch": [None]}, "lunch contains None"),
            ({"dinner": None}, "dinner is None"),
            ({"dinner": [None]}, "dinner contains None"),
            ({"snacks": None}, "snacks is None"),
            ({"snacks": [None]}, "snacks contains None"),
        ]

        for overrides, expected_message in cases:
            with self.subTest(overrides=overrides):
                with self.assertRaisesRegex(Exception, expected_message):
                    self._create_food_log(**overrides)

    def test_to_dict_serializes_nested_food_items(self):
        food_log = self._create_food_log(
            breakfast=[_FakeFood({"description": "oatmeal"})],
            lunch=[_FakeFood({"description": "salad"})],
            dinner=[_FakeFood({"description": "salmon"})],
            snacks=[_FakeFood({"description": "apple"})],
        )

        self.assertEqual(
            {
                "date": dateTime.today().isoformat(),
                "breakfast": [{"description": "oatmeal"}],
                "lunch": [{"description": "salad"}],
                "dinner": [{"description": "salmon"}],
                "snacks": [{"description": "apple"}],
            },
            food_log.toDict(),
        )


if __name__ == "__main__":
    unittest.main()
