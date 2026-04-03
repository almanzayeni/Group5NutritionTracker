'''
Created on Mar 9, 2026

@author: Justin Smith, Yeni Almanza
'''

PROTOCOL = "tcp"
IP_ADDRESS = "127.0.0.1"
PORT = "5555"

KEY_REQUEST_TYPE = "request_type"
KEY_FAILURE_MESSAGE = "failure_message"
EXIT_COMMAND = "exit";
KEY_STATUS = "status"
KEY_USER = "user"
KEY_USERNAME = "username"
KEY_PASSWORD = "password"
KEY_NAME = "name"
KEY_DIET_GOALS = "dietGoals"
KEY_PRIMARY_GOAL = "primaryGoal"
KEY_CALORIE_GOAL = "calorieGoal"
KEY_PROTEIN_GOAL = "proteinGoal"
KEY_FAT_GOAL = "fatGoal"
KEY_SUGAR_GOAL = "sugarGoal"
KEY_SODIUM_GOAL = "sodiumGoal"
KEY_CARBS_GOAL = "carbsGoal"
KEY_OTHER_GOALS = "otherGoals"
KEY_QUERY = "query"
KEY_SEARCH_RESULTS = "search_results"

SUCCESS_STATUS = 1
BAD_MESSAGE_STATUS = -1
UNSUPPORTED_OPERATION_STATUS = -1

AUTHENTICATE_LOGIN_REQUEST_TYPE = "AUTH"
CREATE_ACCOUNT_REQUEST_TYPE = "CREATE_ACCOUNT"
SEARCH_REQUEST_TYPE = "SEARCH"
