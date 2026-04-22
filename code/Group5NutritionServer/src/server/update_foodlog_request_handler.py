'''
Created on April 21, 2026

@author: Justin Smith
'''

from datetime import date
from model import database
from model.food_log import FoodLog
from server import constants

def handleRequest(request):
    if (request == None):
        raise Exception("request is None")
    if (constants.KEY_USERNAME not in request):
        raise Exception("request does not contain username")
    if (constants.KEY_PASSWORD not in request):
        raise Exception("request does not contain password")
    if (constants.KEY_FOOD_LOG not in request):
        raise Exception("request does not contain food log")
    
    username = request[constants.KEY_USERNAME]
    password = request[constants.KEY_PASSWORD]
    foodLogDict = request[constants.KEY_FOOD_LOG]
    dateString = foodLogDict[constants.KEY_DATE]
    dateParts = dateString.split("-");
    request_date = date(int(dateParts[0]), int(dateParts[1]), int(dateParts[2]))
    breakfastItems = []
    lunchItems = []
    dinnerItems = []
    snackItems = []
    
    for food in foodLogDict[constants.KEY_BREAKFAST]:
        breakfastSearchResult = database.searchFoodItemByDescription(food[constants.KEY_FOOD_DESCRIPTION])
        
        if len(breakfastSearchResult) == 0:
            return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: f"food item '{food[constants.KEY_FOOD_DESCRIPTION]}' not found in database"}
        
        breakfastItems.append(breakfastSearchResult[0])
        
    for food in foodLogDict[constants.KEY_LUNCH]:
        lunchSearchResults = database.searchFoodItemByDescription(food[constants.KEY_FOOD_DESCRIPTION])
        
        if len(lunchSearchResults) == 0:
            return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: f"food item '{food[constants.KEY_FOOD_DESCRIPTION]}' not found in database"}
        
        lunchItems.append(lunchSearchResults[0])
    
    for food in foodLogDict[constants.KEY_DINNER]:
        dinnerSearchResults = database.searchFoodItemByDescription(food[constants.KEY_FOOD_DESCRIPTION])
        
        if len(dinnerSearchResults) == 0:
            return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: f"food item '{food[constants.KEY_FOOD_DESCRIPTION]}' not found in database"}
        
        dinnerItems.append(dinnerSearchResults[0])
        
    for food in foodLogDict[constants.KEY_SNACKS]:
        snackSearchResults = database.searchFoodItemByDescription(food[constants.KEY_FOOD_DESCRIPTION])
        
        if len(snackSearchResults) == 0:
            return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: f"food item '{food[constants.KEY_FOOD_DESCRIPTION]}' not found in database"}
        
        snackItems.append(snackSearchResults[0])
    
    foodLog = FoodLog(request_date, breakfastItems, lunchItems, dinnerItems, snackItems)
    
    database.addFoodLog(username, password, foodLog)
    
    try:
        user = database.getUsers()[username][password]
        user.setCurrentFoodLog(foodLog)
    except KeyError:
        return {constants.KEY_STATUS:constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE:"invalid username or password"}
    
    return {constants.KEY_STATUS: constants.SUCCESS_STATUS}