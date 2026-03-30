'''
Created on Mar 30, 2026

@author: OpenAI
'''
import unittest

from model.diet_goals import DietGoals


class TestDietGoals(unittest.TestCase):

    def _create_diet_goals(self, **overrides):
        values = {
            "primaryGoal": "CALORIE",
            "calorieGoal": 2000,
            "proteinGoal": 150,
            "fatGoal": 70,
            "sugarGoal": 50,
            "sodiumGoal": 2300,
            "carbsGoal": 250,
            "otherGoals": ["drink more water"],
        }
        values.update(overrides)
        return DietGoals(**values)

    def test_constructor_sets_all_values(self):
        diet_goals = self._create_diet_goals()

        self.assertEqual("CALORIE", diet_goals.getPrimaryGoal())
        self.assertEqual(2000, diet_goals.getCalorieGoal())
        self.assertEqual(150, diet_goals.getProteinGoal())
        self.assertEqual(70, diet_goals.getFatGoal())
        self.assertEqual(50, diet_goals.getSugarGoal())
        self.assertEqual(2300, diet_goals.getSodiumGoal())
        self.assertEqual(250, diet_goals.getCarbsGoal())
        self.assertEqual(["drink more water"], diet_goals.getOtherGoals())

    def test_constructor_validates_invalid_arguments(self):
        cases = [
            ({"primaryGoal": None}, "primary goal is None"),
            ({"calorieGoal": None}, "calorie goal is None"),
            ({"calorieGoal": -1}, "calorie goal is negative"),
            ({"proteinGoal": None}, "protein goal is None"),
            ({"proteinGoal": -1}, "protein goal is negative"),
            ({"fatGoal": None}, "fat goal is None"),
            ({"fatGoal": -1}, "fat goal is negative"),
            ({"sugarGoal": None}, "sugar goal is None"),
            ({"sugarGoal": -1}, "sugar goal is negative"),
            ({"sodiumGoal": None}, "sodium goal is None"),
            ({"sodiumGoal": -1}, "sodium goal is negative"),
            ({"carbsGoal": None}, "carbs goal is None"),
            ({"carbsGoal": -1}, "carbs goal is negative"),
            ({"otherGoals": None}, "other goals is None"),
            ({"otherGoals": ["drink more water", None]}, "other goals contains None"),
        ]

        for overrides, expected_message in cases:
            with self.subTest(overrides=overrides):
                with self.assertRaisesRegex(Exception, expected_message):
                    self._create_diet_goals(**overrides)

    def test_to_dict_returns_serializable_values(self):
        diet_goals = self._create_diet_goals(otherGoals=["eat more vegetables"])

        self.assertEqual(
            {
                "primaryGoal": "CALORIE",
                "calorieGoal": 2000,
                "proteinGoal": 150,
                "fatGoal": 70,
                "sugarGoal": 50,
                "sodiumGoal": 2300,
                "carbsGoal": 250,
                "otherGoals": ["eat more vegetables"],
            },
            diet_goals.toDict(),
        )


if __name__ == "__main__":
    unittest.main()
