'''
Create account request handler.


@author: Yeni Almanza
@version: spring 2026
'''
from model import database
from model.user import User
from model.food_log import FoodLog
from model.diet_goals import DietGoals
from server import constants
from datetime import date as dateTime


KEY_NAME                    = "name"
KEY_PRIMARY_GOAL            = "primaryGoal"
KEY_CALORIE_GOAL            = "calorieGoal"
KEY_PROTEIN_GOAL            = "proteinGoal"
KEY_FAT_GOAL                = "fatGoal"
KEY_SUGAR_GOAL              = "sugarGoal"
KEY_SODIUM_GOAL             = "sodiumGoal"
KEY_CARBS_GOAL              = "carbsGoal"
KEY_OTHER_GOALS             = "otherGoals"
CREATE_ACCOUNT_REQUEST_TYPE = "CREATE_ACCOUNT"


def handleRequest(request):
    '''
    Validates the provided account creation data, reads the diet goals
    entered by the user, and creates a new User.

    @precondition request != None &&
<<<<<<< feature-ServerSearch
                  request contains keys:
                    constants.KEY_USERNAME,
                    constants.KEY_PASSWORD,
                    constants.KEY_NAME
=======
                  request contains keys: username, password, name,
                  primaryGoal, calorieGoal, proteinGoal, fatGoal,
                  sugarGoal, sodiumGoal, carbsGoal, otherGoals
>>>>>>> main

    @param request  the parsed JSON dict sent by the Java client

    @return a response dict:
              success -> {status: 1, user: <user dict>}
              failure -> {status: -1, failure_message: <reason>}
    '''
    if request is None:
        raise Exception("request is None")
    if constants.KEY_USERNAME not in request:
        raise Exception("request does not contain username")
    if constants.KEY_PASSWORD not in request:
        raise Exception("request does not contain password")
    if constants.KEY_NAME not in request:
        raise Exception("request does not contain name")
    if KEY_PRIMARY_GOAL not in request:
        raise Exception("request does not contain primaryGoal")
    if KEY_CALORIE_GOAL not in request:
        raise Exception("request does not contain calorieGoal")
    if KEY_PROTEIN_GOAL not in request:
        raise Exception("request does not contain proteinGoal")
    if KEY_FAT_GOAL not in request:
        raise Exception("request does not contain fatGoal")
    if KEY_SUGAR_GOAL not in request:
        raise Exception("request does not contain sugarGoal")
    if KEY_SODIUM_GOAL not in request:
        raise Exception("request does not contain sodiumGoal")
    if KEY_CARBS_GOAL not in request:
        raise Exception("request does not contain carbsGoal")
    if KEY_OTHER_GOALS not in request:
        raise Exception("request does not contain otherGoals")

    username = request[constants.KEY_USERNAME]
    password = request[constants.KEY_PASSWORD]
    name     = request[constants.KEY_NAME]

    if username in database.getUsers():
        return {
            constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
            constants.KEY_FAILURE_MESSAGE: "username already exists"
        }

    diet_goals = DietGoals(
        primaryGoal=request[KEY_PRIMARY_GOAL],
        calorieGoal=request[KEY_CALORIE_GOAL],
        proteinGoal=request[KEY_PROTEIN_GOAL],
        fatGoal=request[KEY_FAT_GOAL],
        sugarGoal=request[KEY_SUGAR_GOAL],
        sodiumGoal=request[KEY_SODIUM_GOAL],
        carbsGoal=request[KEY_CARBS_GOAL],
        otherGoals=request[KEY_OTHER_GOALS]
    )

    food_log = FoodLog(
        date=dateTime.today(),
        breakfast=[],
        lunch=[],
        dinner=[],
        snacks=[]
    )

    new_user = User(name, username, password, food_log, diet_goals)

    database.getUsers()[username] = {password: new_user}

    return {
        constants.KEY_STATUS: constants.SUCCESS_STATUS,
        constants.KEY_USER: new_user.toDict()
    }