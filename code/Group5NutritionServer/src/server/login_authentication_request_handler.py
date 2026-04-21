'''
Created on Mar 9, 2026

@author: Justin Smith
'''
from datetime import date
from model import database
from model.food_log import FoodLog
from server import constants

def handleRequest(request):
    '''
    Validates the provided login credentials and returns the appropriate response.
    @precondition request != None &&
                  request contains keys constants.KEY_USERNAME and constants.KEY_PASSWORD
        
    @return a response containing the status of the request and, if successful, the user associated with the provided credentials.
    '''
    if (request == None):
        raise Exception("request is None")
    if (constants.KEY_USERNAME not in request):
        raise Exception("request does not contain username")
    if (constants.KEY_PASSWORD not in request):
        raise Exception("request does not contain password")
        
    username = request[constants.KEY_USERNAME]
    password = request[constants.KEY_PASSWORD]
    user = None
    
    try:
        user = database.getUsers()[username][password]
    except KeyError:
        return {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"invalid username or password"}
    
    if user.getCurrentFoodLog().getDate() != date.today():
        user._currentFoodLog = database.searchFoodLogByDate(username, date.today())
        if user.getCurrentFoodLog() == None:
            user._currentFoodLog = FoodLog(date.today(), [], [], [], [])
            database.addFoodLog(username, user.getCurrentFoodLog())
    
    return {constants.KEY_STATUS:constants.SUCCESS_STATUS, constants.KEY_USER:user.toDict()}