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





def handleRequest(request):
    '''
    Validates the provided account creation data, reads the diet goals
    entered by the user, and creates a new User.

    @precondition request != None &&
                  request contains constants.KEY_USER

    @param request  the parsed JSON dict sent by the Java client

    @return a response dict:
              success -> {status: 1, user: <user dict>}
              failure -> {status: -1, failure_message: <reason>}
    '''
    if request is None:
        raise Exception("request is None")
    if constants.KEY_USER not in request:
        raise Exception("request does not contain user.")

    username = request[constants.KEY_USER][constants.KEY_USERNAME]

    if username in database.getUsers():
        return {
            constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
            constants.KEY_FAILURE_MESSAGE: "username already exists"
        }
        
    password = request[constants.KEY_USER][constants.KEY_PASSWORD]
    name     = request[constants.KEY_USER][constants.KEY_NAME]

    diet_goals = DietGoals(
        primaryGoal=request[constants.KEY_USER][constants.KEY_DIET_GOALS][constants.KEY_PRIMARY_GOAL],
        calorieGoal=request[constants.KEY_USER][constants.KEY_DIET_GOALS][constants.KEY_CALORIE_GOAL],
        proteinGoal=request[constants.KEY_USER][constants.KEY_DIET_GOALS][constants.KEY_PROTEIN_GOAL],
        fatGoal=request[constants.KEY_USER][constants.KEY_DIET_GOALS][constants.KEY_FAT_GOAL],
        sugarGoal=request[constants.KEY_USER][constants.KEY_DIET_GOALS][constants.KEY_SUGAR_GOAL],
        sodiumGoal=request[constants.KEY_USER][constants.KEY_DIET_GOALS][constants.KEY_SODIUM_GOAL],
        carbsGoal=request[constants.KEY_USER][constants.KEY_DIET_GOALS][constants.KEY_CARBS_GOAL],
        otherGoals=request[constants.KEY_USER][constants.KEY_DIET_GOALS][constants.KEY_OTHER_GOALS]
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