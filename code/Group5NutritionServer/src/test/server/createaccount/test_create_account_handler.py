'''
Unit tests for create_account_request_handler.

Covers every branch in handleRequest() for 100% branch coverage.

@author: Yeni Almanza
@version: spring 2026
'''
import unittest
from model import database
from server import constants
from server.create_account_request_handler import (
    handleRequest,
    KEY_NAME,
    KEY_PRIMARY_GOAL,
    KEY_CALORIE_GOAL,
    KEY_PROTEIN_GOAL,
    KEY_FAT_GOAL,
    KEY_SUGAR_GOAL,
    KEY_SODIUM_GOAL,
    KEY_CARBS_GOAL,
    KEY_OTHER_GOALS,
)


def _valid_request(**overrides):
    '''
    Returns a fully populated valid request dict.
    Pass keyword args to remove or override specific fields, e.g.
        _valid_request(calorieGoal=None)  -- replaces value
        _valid_request()                   -- complete valid request
    Use _request_without(key) to drop a key entirely.
    '''
    base = {
        constants.KEY_USERNAME: "newuser",
        constants.KEY_PASSWORD: "newpass",
        KEY_NAME:               "New User",
        KEY_PRIMARY_GOAL:       "CALORIE",
        KEY_CALORIE_GOAL:       2000,
        KEY_PROTEIN_GOAL:       150,
        KEY_FAT_GOAL:           70,
        KEY_SUGAR_GOAL:         50,
        KEY_SODIUM_GOAL:        2300,
        KEY_CARBS_GOAL:         250,
        KEY_OTHER_GOALS:        [],
    }
    base.update(overrides)
    return base


def _request_without(key):
    '''Returns a valid request with the given key removed entirely.'''
    req = _valid_request()
    del req[key]
    return req


class TestCreateAccountRequestHandler(unittest.TestCase):

    def setUp(self):
        '''Clear the in-memory users dict before each test.'''
        database.getUsers().clear()


    def test_handleRequest_noneRequest_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(None)
        self.assertEqual(str(context.exception), "request is None")

    def test_handleRequest_missingUsername_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(constants.KEY_USERNAME))
        self.assertEqual(str(context.exception), "request does not contain username")

    def test_handleRequest_missingPassword_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(constants.KEY_PASSWORD))
        self.assertEqual(str(context.exception), "request does not contain password")

    def test_handleRequest_missingName_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(KEY_NAME))
        self.assertEqual(str(context.exception), "request does not contain name")

    def test_handleRequest_missingPrimaryGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(KEY_PRIMARY_GOAL))
        self.assertEqual(str(context.exception), "request does not contain primaryGoal")

    def test_handleRequest_missingCalorieGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(KEY_CALORIE_GOAL))
        self.assertEqual(str(context.exception), "request does not contain calorieGoal")

    def test_handleRequest_missingProteinGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(KEY_PROTEIN_GOAL))
        self.assertEqual(str(context.exception), "request does not contain proteinGoal")

    def test_handleRequest_missingFatGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(KEY_FAT_GOAL))
        self.assertEqual(str(context.exception), "request does not contain fatGoal")

    def test_handleRequest_missingSugarGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(KEY_SUGAR_GOAL))
        self.assertEqual(str(context.exception), "request does not contain sugarGoal")

    def test_handleRequest_missingSodiumGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(KEY_SODIUM_GOAL))
        self.assertEqual(str(context.exception), "request does not contain sodiumGoal")

    def test_handleRequest_missingCarbsGoal_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(KEY_CARBS_GOAL))
        self.assertEqual(str(context.exception), "request does not contain carbsGoal")

    def test_handleRequest_missingOtherGoals_raisesException(self):
        with self.assertRaises(Exception) as context:
            handleRequest(_request_without(KEY_OTHER_GOALS))
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
        self.assertEqual(diet_goals["fatGoal"],     70)
        self.assertEqual(diet_goals["sugarGoal"],   50)
        self.assertEqual(diet_goals["sodiumGoal"],  2300)
        self.assertEqual(diet_goals["carbsGoal"],   250)

    def test_handleRequest_validRequest_customDietGoalsStoredCorrectly(self):
        request = _valid_request(
            **{
                KEY_CALORIE_GOAL: 1800,
                KEY_PROTEIN_GOAL: 120,
                KEY_FAT_GOAL:     60,
                KEY_SUGAR_GOAL:   30,
                KEY_SODIUM_GOAL:  1500,
                KEY_CARBS_GOAL:   200,
                KEY_OTHER_GOALS:  ["vegan"],
            }
        )
        response = handleRequest(request)
        diet_goals = response[constants.KEY_USER]["dietGoals"]
        self.assertEqual(diet_goals["calorieGoal"], 1800)
        self.assertEqual(diet_goals["proteinGoal"], 120)
        self.assertEqual(diet_goals["otherGoals"],  ["vegan"])

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