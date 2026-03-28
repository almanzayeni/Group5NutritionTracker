'''
Unit tests for create_account_request_handler.

Covers every branch in handleRequest() for 100% branch coverage.

@author: Yeni Almanza
@version: spring 2026
'''
import unittest
from unittest.mock import patch
from model import database
from server import constants
from server.create_account_request_handler import handleRequest, KEY_NAME


class TestCreateAccountRequestHandler(unittest.TestCase):

    def setUp(self):
        '''
        Clear the in-memory users dict before each test so tests
        do not bleed into one another.
        '''
        database.getUsers().clear()

    def test_handleRequest_noneRequest_raisesException(self):
        '''
        Branch: request is None -> raises Exception
        '''
        with self.assertRaises(Exception) as context:
            handleRequest(None)
        self.assertEqual(str(context.exception), "request is None")

    def test_handleRequest_missingUsername_raisesException(self):
        '''
        Branch: KEY_USERNAME not in request -> raises Exception
        '''
        request = {
            constants.KEY_PASSWORD: "pass123",
            KEY_NAME: "John Doe"
        }
        with self.assertRaises(Exception) as context:
            handleRequest(request)
        self.assertEqual(str(context.exception), "request does not contain username")

    def test_handleRequest_missingPassword_raisesException(self):
        '''
        Branch: KEY_PASSWORD not in request -> raises Exception
        '''
        request = {
            constants.KEY_USERNAME: "johndoe",
            KEY_NAME: "John Doe"
        }
        with self.assertRaises(Exception) as context:
            handleRequest(request)
        self.assertEqual(str(context.exception), "request does not contain password")

    def test_handleRequest_missingName_raisesException(self):
        '''
        Branch: KEY_NAME not in request -> raises Exception
        '''
        request = {
            constants.KEY_USERNAME: "johndoe",
            constants.KEY_PASSWORD: "pass123"
        }
        with self.assertRaises(Exception) as context:
            handleRequest(request)
        self.assertEqual(str(context.exception), "request does not contain name")


    def test_handleRequest_duplicateUsername_returnsFailureResponse(self):
        '''
        Branch: username already in database -> returns failure response
        '''
        database.getUsers()["johndoe"] = {"pass123": object()}

        request = {
            constants.KEY_USERNAME: "johndoe",
            constants.KEY_PASSWORD: "pass123",
            KEY_NAME: "John Doe"
        }
        response = handleRequest(request)

        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
        self.assertEqual(response[constants.KEY_FAILURE_MESSAGE], "username already exists")

    def test_handleRequest_validNewUser_returnsSuccessResponse(self):
        '''
        Branch: all fields valid, username not taken -> returns success response
        '''
        request = {
            constants.KEY_USERNAME: "newuser",
            constants.KEY_PASSWORD: "newpass",
            KEY_NAME: "New User"
        }
        response = handleRequest(request)

        self.assertEqual(response[constants.KEY_STATUS], constants.SUCCESS_STATUS)
        self.assertIn(constants.KEY_USER, response)

    def test_handleRequest_validNewUser_storesUserInDatabase(self):
        '''
        Branch: success -> user is actually saved to database
        '''
        request = {
            constants.KEY_USERNAME: "newuser",
            constants.KEY_PASSWORD: "newpass",
            KEY_NAME: "New User"
        }
        handleRequest(request)

        self.assertIn("newuser", database.getUsers())
        self.assertIn("newpass", database.getUsers()["newuser"])

    def test_handleRequest_validNewUser_returnedUserDictHasCorrectUsername(self):
        '''
        Branch: success -> returned user dict contains the correct username
        '''
        request = {
            constants.KEY_USERNAME: "newuser",
            constants.KEY_PASSWORD: "newpass",
            KEY_NAME: "New User"
        }
        response = handleRequest(request)

        self.assertEqual(response[constants.KEY_USER]["username"], "newuser")

    def test_handleRequest_validNewUser_returnedUserDictHasCorrectName(self):
        '''
        Branch: success -> returned user dict contains the correct name
        '''
        request = {
            constants.KEY_USERNAME: "newuser",
            constants.KEY_PASSWORD: "newpass",
            KEY_NAME: "New User"
        }
        response = handleRequest(request)

        self.assertEqual(response[constants.KEY_USER]["name"], "New User")

    def test_handleRequest_twoDistinctUsers_bothStoredSuccessfully(self):
        '''
        Branch: success path called twice with different usernames ->
        both users exist in database independently
        '''
        request1 = {
            constants.KEY_USERNAME: "user1",
            constants.KEY_PASSWORD: "pass1",
            KEY_NAME: "User One"
        }
        request2 = {
            constants.KEY_USERNAME: "user2",
            constants.KEY_PASSWORD: "pass2",
            KEY_NAME: "User Two"
        }
        handleRequest(request1)
        handleRequest(request2)

        self.assertIn("user1", database.getUsers())
        self.assertIn("user2", database.getUsers())

    def test_handleRequest_secondRequestSameUsername_returnsFailure(self):
        '''
        Branch: first request succeeds, second with same username hits
        the duplicate branch -> failure response
        '''
        request = {
            constants.KEY_USERNAME: "newuser",
            constants.KEY_PASSWORD: "newpass",
            KEY_NAME: "New User"
        }
        handleRequest(request)
        response = handleRequest(request)

        self.assertEqual(response[constants.KEY_STATUS], constants.BAD_MESSAGE_STATUS)
        self.assertEqual(response[constants.KEY_FAILURE_MESSAGE], "username already exists")


if __name__ == "__main__":
    unittest.main()
