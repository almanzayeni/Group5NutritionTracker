'''
Created on April 24, 2026

@author: Justin Smith
'''

from model import database
from server import constants
from model.diet_goals import DietGoals

def handleRequest(request):
    if (request == None):
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "request is None"}
    if (constants.KEY_USERNAME not in request):
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "request does not contain username"}
    if (constants.KEY_PASSWORD not in request):
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "request does not contain password"}
    if (constants.KEY_DIET_GOALS not in request):
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "request does not contain diet goals"}
    
    username = request[constants.KEY_USERNAME]
    password = request[constants.KEY_PASSWORD]
    dietGoalsDict = request[constants.KEY_DIET_GOALS]
    
    try:
        newDietGoals = DietGoals(dietGoalsDict[constants.KEY_PRIMARY_GOAL], dietGoalsDict[constants.KEY_CALORIE_GOAL], dietGoalsDict[constants.KEY_PROTEIN_GOAL], dietGoalsDict[constants.KEY_FAT_GOAL], dietGoalsDict[constants.KEY_SUGAR_GOAL], dietGoalsDict[constants.KEY_SODIUM_GOAL], dietGoalsDict[constants.KEY_CARBS_GOAL], dietGoalsDict[constants.KEY_OTHER_GOALS])
    except Exception as e:
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: f"invalid diet goals: {str(e)}"}
    
    user = None
    
    try:
        user = database.getUser(username, password)
    except KeyError:
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "invalid username or password"}
    except Exception:
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "Username or Password is None"}
    
    user.setDietGoals(newDietGoals)
    
    database.addUser(user)
    
    return {constants.KEY_STATUS: constants.SUCCESS_STATUS}