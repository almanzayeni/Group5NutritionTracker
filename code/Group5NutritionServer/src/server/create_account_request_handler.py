'''
Create account request handler.

Validates the incoming request, checks for duplicate usernames,
creates a new User, and stores it in the database.

@author: Yeni Almanza
@version: spring 2026
'''
import json
from model import database
from model.user import User
from model.food_log import FoodLog
from model.diet_goals import DietGoals
from server import constants
from datetime import date as dateTime


def handleRequest(request):
    '''
    Validates the provided account creation data and creates a new user.

    @precondition request != None &&
                  request contains keys:
                    constants.KEY_USERNAME,
                    constants.KEY_PASSWORD,
                    constants.KEY_NAME

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

    username = request[constants.KEY_USERNAME]
    password = request[constants.KEY_PASSWORD]
    name     = request[constants.KEY_NAME]

    if username in database.getUsers():
        return {
            constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
            constants.KEY_FAILURE_MESSAGE: "username already exists"
        }

    diet_goals = DietGoals(
        primaryGoal="CALORIE",
        calorieGoal=2000,
        proteinGoal=150,
        fatGoal=70,
        sugarGoal=50,
        sodiumGoal=2300,
        carbsGoal=250,
        otherGoals=[]
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


KEY_NAME                    = "name"
CREATE_ACCOUNT_REQUEST_TYPE = "CREATE_ACCOUNT"
