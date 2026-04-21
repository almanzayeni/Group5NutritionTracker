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

def _require_key(data, key, message):
    if not isinstance(data, dict) or key not in data:
        raise Exception(message)
    return data[key]

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
    
    user_request = _require_key(
        request,
        constants.KEY_USER,
        "request does not contain user.",
    )
    username = _require_key(
        user_request,
        constants.KEY_USERNAME,
        "request does not contain username",
    )

    if username in database.getUsers():
        return {
            constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
            constants.KEY_FAILURE_MESSAGE: "username already exists"
        }
        
    password = _require_key(
        user_request,
        constants.KEY_PASSWORD,
        "request does not contain password",
    )
    name = _require_key(
        user_request,
        constants.KEY_NAME,
        "request does not contain name",
    )
    diet_goals_request = _require_key(
        user_request,
        constants.KEY_DIET_GOALS,
        "request does not contain dietGoals",
    )

    diet_goals = DietGoals(
        primaryGoal=_require_key(
            diet_goals_request,
            constants.KEY_PRIMARY_GOAL,
            "request does not contain primaryGoal",
        ),
        calorieGoal=_require_key(
            diet_goals_request,
            constants.KEY_CALORIE_GOAL,
            "request does not contain calorieGoal",
        ),
        proteinGoal=_require_key(
            diet_goals_request,
            constants.KEY_PROTEIN_GOAL,
            "request does not contain proteinGoal",
        ),
        fatGoal=_require_key(
            diet_goals_request,
            constants.KEY_FAT_GOAL,
            "request does not contain fatGoal",
        ),
        sugarGoal=_require_key(
            diet_goals_request,
            constants.KEY_SUGAR_GOAL,
            "request does not contain sugarGoal",
        ),
        sodiumGoal=_require_key(
            diet_goals_request,
            constants.KEY_SODIUM_GOAL,
            "request does not contain sodiumGoal",
        ),
        carbsGoal=_require_key(
            diet_goals_request,
            constants.KEY_CARBS_GOAL,
            "request does not contain carbsGoal",
        ),
        otherGoals=_require_key(
            diet_goals_request,
            constants.KEY_OTHER_GOALS,
            "request does not contain otherGoals",
        ),
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
    database.addFoodLog(username, food_log)

    return {
        constants.KEY_STATUS: constants.SUCCESS_STATUS,
        constants.KEY_USER: new_user.toDict()
    }
