'''
Created on Mar 9, 2026

@author: Justin Smith, Yeni Almanza, Emi Collins
'''

PROTOCOL = "tcp"
IP_ADDRESS = "127.0.0.1"
PORT = "5555"

KEY_REQUEST_TYPE = "request_type"
KEY_FAILURE_MESSAGE = "failure_message"
EXIT_COMMAND = "exit";
KEY_SUCCESS_MESSAGE = "success_message"
KEY_SERVER_EXIT = "Server exited successfully"
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
KEY_FOOD_ITEM = "food_item"
KEY_FOOD_TYPE = "type"
KEY_BASE_FOOD_TYPE = "base"
KEY_COMPOSITE_FOOD_TYPE = "composite"
KEY_INGREDIENTS = "ingredients"
KEY_DATE = "date"
KEY_FOOD_LOG = "foodLog"

KEY_FOOD_DESCRIPTION = "description"
KEY_FOOD_QUANTITY_CATEGORY = "quantityCategory"
KEY_FOOD_PORTION_SIZE = "portionSize"
KEY_FOOD_CALORIES = "calories"
KEY_FOOD_PROTEIN = "protein"
KEY_FOOD_FAT = "fat"
KEY_FOOD_SUGAR = "sugar"
KEY_FOOD_CARBS = "carbohydrates"
KEY_FOOD_SODIUM = "sodium"

SUCCESS_STATUS = 1
BAD_MESSAGE_STATUS = -1
UNSUPPORTED_OPERATION_STATUS = -1

AUTHENTICATE_LOGIN_REQUEST_TYPE = "AUTH"
ADD_FOOD_REQUEST_TYPE = "ADD_FOOD"
CREATE_ACCOUNT_REQUEST_TYPE = "CREATE_ACCOUNT"
SEARCH_REQUEST_TYPE = "SEARCH"
GET_DAY_OF_FOOD_REQUEST_TYPE = "GET_DAY_OF_FOOD"
