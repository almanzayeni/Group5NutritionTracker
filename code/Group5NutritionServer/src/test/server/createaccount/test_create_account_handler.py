'''
Unit tests for create_account_request_handler.

@author: Yeni Almanza
@version: spring 2026
'''
import unittest

from model import database
from server import constants
from server.create_account_request_handler import handleRequest


def _valid_request(
    username="newuser",
    password="newpass",
    name="New User",
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


def _request_without_user_key(key):
    request = _valid_request()
    del request[constants.KEY_USER][key]
    return request


def _request_without_goal_key(key):
    request = _valid_request()
    del request[constants.KEY_USER][constants.KEY_DIET_GOALS][key]
    return request


class TestCreateAccountRequestHandler(unittest.TestCase):

    def setUp(self):
        database.getUsers().clear()

    def test_handleRequest_noneRequest_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(None)
        self.assertEqual(str(context.exception), "request is None")

    def test_handleRequest_missingUser_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest({})
        self.assertEqual(str(context.exception), "request does not contain user.")

    def test_handleRequest_missingUsername_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_user_key(constants.KEY_USERNAME))
        self.assertEqual(str(context.exception), "request does not contain username")

    def test_handleRequest_missingPassword_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_user_key(constants.KEY_PASSWORD))
        self.assertEqual(str(context.exception), "request does not contain password")

    def test_handleRequest_missingName_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_user_key(constants.KEY_NAME))
        self.assertEqual(str(context.exception), "request does not contain name")

    def test_handleRequest_missingDietGoals_raisesException(self):
        request = _valid_request()
        del request[constants.KEY_USER][constants.KEY_DIET_GOALS]

        with self.assertRaises(Exception) as context:
            handleRequest(request)
        self.assertEqual(str(context.exception), "request does not contain dietGoals")

    def test_handleRequest_missingPrimaryGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_goal_key(constants.KEY_PRIMARY_GOAL))
        self.assertEqual(str(context.exception), "request does not contain primaryGoal")

    def test_handleRequest_missingCalorieGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_goal_key(constants.KEY_CALORIE_GOAL))
        self.assertEqual(str(context.exception), "request does not contain calorieGoal")

    def test_handleRequest_missingProteinGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_goal_key(constants.KEY_PROTEIN_GOAL))
        self.assertEqual(str(context.exception), "request does not contain proteinGoal")

    def test_handleRequest_missingFatGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_goal_key(constants.KEY_FAT_GOAL))
        self.assertEqual(str(context.exception), "request does not contain fatGoal")

    def test_handleRequest_missingSugarGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_goal_key(constants.KEY_SUGAR_GOAL))
        self.assertEqual(str(context.exception), "request does not contain sugarGoal")

    def test_handleRequest_missingSodiumGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_goal_key(constants.KEY_SODIUM_GOAL))
        self.assertEqual(str(context.exception), "request does not contain sodiumGoal")

    def test_handleRequest_missingCarbsGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_goal_key(constants.KEY_CARBS_GOAL))
        self.assertEqual(str(context.exception), "request does not contain carbsGoal")

    def test_handleRequest_missingOtherGoals_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without_goal_key(constants.KEY_OTHER_GOALS))
        self.assertEqual(str(context.exception), "request does not contain otherGoals")

    def test_handleRequest_duplicateUsername_returnsFailureResponse(self):
        database.getUsers()["newuser"] = {"newpass": object()}

        response = handleRequest(_valid_request())

        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
        self.assertEqual(response[constants.KEY_FAILURE_MESSAGE], "username already exists")

    def test_handleRequest_validRequest_returnsSuccessStatus(self):
        response = handleRequest(_valid_request())
        self.assertEqual(response[constants.KEY_STATUS], constants.SUCCESS_STATUS)

    def test_handleRequest_validRequest_responseContainsUser(self):
        response = handleRequest(_valid_request())
        self.assertIn(constants.KEY_USER, response)

    def test_handleRequest_validRequest_storesUserInDatabase(self):
        handleRequest(_valid_request())
        self.assertIn("newuser", database.getUsers())
        self.assertIn("newpass", database.getUsers()["newuser"])

    def test_handleRequest_validRequest_returnedUserHasCorrectUsername(self):
        response = handleRequest(_valid_request())
        self.assertEqual(response[constants.KEY_USER]["username"], "newuser")

    def test_handleRequest_validRequest_returnedUserHasCorrectName(self):
        response = handleRequest(_valid_request())
        self.assertEqual(response[constants.KEY_USER]["name"], "New User")

    def test_handleRequest_validRequest_dietGoalsStoredFromRequest(self):
        response = handleRequest(_valid_request())
        diet_goals = response[constants.KEY_USER]["dietGoals"]
        self.assertEqual(diet_goals["primaryGoal"], "CALORIE")
        self.assertEqual(diet_goals["calorieGoal"], 2000)
        self.assertEqual(diet_goals["proteinGoal"], 150)
        self.assertEqual(diet_goals["fatGoal"], 70)
        self.assertEqual(diet_goals["sugarGoal"], 50)
        self.assertEqual(diet_goals["sodiumGoal"], 2300)
        self.assertEqual(diet_goals["carbsGoal"], 250)

    def test_handleRequest_validRequest_customDietGoalsStoredCorrectly(self):
        response = handleRequest(
            _valid_request(
                calorie_goal=1800,
                protein_goal=120,
                fat_goal=60,
                sugar_goal=30,
                sodium_goal=1500,
                carbs_goal=200,
                other_goals=["vegan"],
            )
        )

        diet_goals = response[constants.KEY_USER]["dietGoals"]
        self.assertEqual(diet_goals["calorieGoal"], 1800)
        self.assertEqual(diet_goals["proteinGoal"], 120)
        self.assertEqual(diet_goals["otherGoals"], ["vegan"])

    def test_handleRequest_twoDistinctUsers_bothStoredSuccessfully(self):
        handleRequest(_valid_request(username="user1", password="pass1", name="User One"))
        handleRequest(_valid_request(username="user2", password="pass2", name="User Two"))

        self.assertIn("user1", database.getUsers())
        self.assertIn("user2", database.getUsers())

    def test_handleRequest_secondRequestSameUsername_returnsFailure(self):
        handleRequest(_valid_request())

        response = handleRequest(_valid_request())

        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
        self.assertEqual(response[constants.KEY_FAILURE_MESSAGE], "username already exists")


if __name__ == "__main__":
    unittest.main()
