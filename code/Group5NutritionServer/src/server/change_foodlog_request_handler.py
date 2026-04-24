'''
Created on April 23, 2026

@author: Justin Smith
'''

from datetime import date
from model import database
from model.food_log import FoodLog
from server import constants

def handelRequest(request):
    '''
    Retrieves the food log for the specified user and date from the database and returns the appropriate response.
    
    @precondition request != None &&
                  request contains key constants.KEY_USERNAME &&
                  request contains key constants.KEY_DATE
                  
    @return a response containing the status of the request and, if successful, the food log for the specified user and date.
    '''
    
    if (request == None):
        return {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"There was no request"}
    if (constants.KEY_USERNAME not in request):
        return {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"request does not contain username"}
    if (constants.KEY_PASSWORD not in request):
        return {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"request does not contain password"}
    if (constants.KEY_DATE not in request):
        return {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"request does not contain date"}
    
    username = request[constants.KEY_USERNAME]
    password = request[constants.KEY_PASSWORD]
    requestDate = request[constants.KEY_DATE]
    dateParts = requestDate.split("-")
    
    if (len(dateParts) != 3):
        return {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"date is not in the correct format"}
    
    for x in range(3):
        if (not dateParts[x].isdigit()):
            return {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"date is not in the correct format"}
        dateParts[x] = int(dateParts[x])
    
    formattedDate = date(dateParts[0], dateParts[1], dateParts[2])
    
    foodLog = None
    
    try:
        foodLog = database.searchFoodLogByDate(username, password, formattedDate)
    except Exception as e:
        return {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:str(e)}
    
    if (foodLog == None):
        foodLog = FoodLog(formattedDate, [], [], [], [])
    
    return {constants.KEY_STATUS:constants.SUCCESS_STATUS, constants.KEY_FOOD_LOG:foodLog.toDict()}
