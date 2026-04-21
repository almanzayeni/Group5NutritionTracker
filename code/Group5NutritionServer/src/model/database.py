'''
Created on Mar 11, 2026

@author: Justin Smith
'''
from datetime import date as date
from model.user import User
from model.food_log import FoodLog
from model.diet_goals import DietGoals
from model.base_food import BaseFood
from model.composite_food import CompositeFood
from model.quantity_category import QuantityCategory

_users = {}
_foodItems = {}
_foodLogs = {}

def loadDefaultData():
    '''
    Loads default data into the database for testing purposes.
    '''
    oatmeal = BaseFood("oatmeal", QuantityCategory.SERVING, 1, 150, 5, 3, 1, 27, 0)
    banana = BaseFood("banana", QuantityCategory.QUANTITY, 1, 105, 1, 0, 14, 27, 1)
    chicken_breast = BaseFood("chicken breast", QuantityCategory.WEIGHT, 1, 165, 31, 4, 0, 0, 74)
    salad = BaseFood("salad", QuantityCategory.WEIGHT, 1, 100, 5, 2, 5, 10, 200)
    chicken_salad = CompositeFood("chicken salad", QuantityCategory.SERVING, 1, 350, 30, 20, 5, 10, 500, {chicken_breast.get_description(): chicken_breast, salad.get_description(): salad})
    grilled_salmon = BaseFood("grilled salmon", QuantityCategory.WEIGHT, 1, 200, 22, 12, 0, 0, 50)
    steamed_vegetables = BaseFood("steamed vegetables", QuantityCategory.WEIGHT, 1, 50, 2, 0, 5, 10, 100)
    apple = BaseFood("apple", QuantityCategory.QUANTITY, 1, 95, 0, 0, 19, 25, 2)
    beef_patty = BaseFood("beef patty", QuantityCategory.WEIGHT, 1, 250, 20, 15, 0, 0, 80)
    pickles = BaseFood("pickles", QuantityCategory.WEIGHT, 1, 10, 0, 0, 2, 3, 300)
    cheese_slice = BaseFood("cheese slice", QuantityCategory.QUANTITY, 1, 70, 5, 6, 0, 1, 150)
    ketchup = BaseFood("ketchup", QuantityCategory.WEIGHT, 1, 20, 0, 0, 5, 5, 150)
    hamburger_bun = BaseFood("hamburger bun", QuantityCategory.QUANTITY, 1, 120, 4, 2, 3, 22, 200)
    hamburger = CompositeFood("hamburger", QuantityCategory.SERVING, 1, 500, 30, 25, 5, 10, 500, {beef_patty.get_description(): beef_patty, pickles.get_description(): pickles, cheese_slice.get_description(): cheese_slice, ketchup.get_description(): ketchup, hamburger_bun.get_description(): hamburger_bun})

    _foodItems[oatmeal.get_description()] = oatmeal
    _foodItems[banana.get_description()] = banana
    _foodItems[chicken_breast.get_description()] = chicken_breast
    _foodItems[salad.get_description()] = salad
    _foodItems[chicken_salad.get_description()] = chicken_salad
    _foodItems[grilled_salmon.get_description()] = grilled_salmon
    _foodItems[steamed_vegetables.get_description()] = steamed_vegetables
    _foodItems[apple.get_description()] = apple
    _foodItems[beef_patty.get_description()] = beef_patty
    _foodItems[pickles.get_description()] = pickles
    _foodItems[cheese_slice.get_description()] = cheese_slice
    _foodItems[ketchup.get_description()] = ketchup
    _foodItems[hamburger_bun.get_description()] = hamburger_bun
    _foodItems[hamburger.get_description()] = hamburger

    foodLog1 = FoodLog(date.today(), [oatmeal, banana], [chicken_salad], [hamburger, steamed_vegetables], [apple])
    foodLog2 = FoodLog(date(2026, 4, 19), [oatmeal], [grilled_salmon], [steamed_vegetables], [])
    dietGoals1 = DietGoals("CALORIE", 2000, 150, 70, 50, 2300, 250, ["eat more vegetables"])
    user1 = User("John Doe", "johndoe", "password123", foodLog1, dietGoals1)

    _users[user1.getUsername()] = {user1.getPassword(): user1}
    _foodLogs[user1.getUsername()] = {foodLog1.getDate(): foodLog1}
    _foodLogs[user1.getUsername()][foodLog2.getDate()] = foodLog2

def getUsers():
    '''
    Returns the users in the database.
    '''
    return _users

def getFoodItems():
    '''
    Returns the food items in the database.
    '''
    return _foodItems

def getFoodLogs():
    '''
    Returns the food logs in the database.
    '''
    return _foodLogs

def addUser(user):
    ''' 
    Adds a user to the database.
        
    @precondition user != None
    '''
    if user is None:
        raise Exception("user is None")
    _users[user.getUsername()] = {user.getPassword(): user}

def addFoodItem(foodItem):
    '''
    Adds a food item to the database.
    
    @precondition foodItem != None
    '''
    if foodItem is None:
        raise Exception("food item is None")
    _foodItems[foodItem.get_description()] = foodItem

def addFoodLog(username, foodLog):
    '''
    Adds a food log to the database for the specified user and date.
    
    @precondition username != None &&
                  foodLog != None
    '''
    if username is None:
        raise Exception("username is None")
    if foodLog is None:
        raise Exception("food log is None")
    if username not in _foodLogs:
        _foodLogs[username] = {}
    _foodLogs[username][foodLog.getDate()] = foodLog
    
def updateFoodLog(username, foodLog):
    '''
    Updates the food log in the database for the specified user and date.
    
    @precondition username != None &&
                  foodLog != None
    '''
    if username is None:
        raise Exception("username is None")
    if foodLog is None:
        raise Exception("food log is None")
    if username not in _foodLogs:
        raise Exception("user not found")
    _foodLogs[username][foodLog.getDate()] = foodLog

def searchFoodItemByDescription(query):
    '''
    Searches the database for food items matching the provided query and returns a list of matching food items.
    
    @precondition query != None
    
    @return a list of food items matching the provided query.
    '''
    if query is None:
        raise Exception("query is None")
    if query == "":
        return []

    foodItems = []
    for key, value in _foodItems.items():
        if query.lower() in key.lower():
            foodItems.append(value)
    return foodItems

def searchFoodLogByDate(username, date):
    '''
    Searches the database for the food log for the specified user and date and returns the food log if found.
    
    @precondition username != None &&
                  date != None
                  
    @return the food log for the specified user and date if found, otherwise None if no matching date is found for the user.
    '''
    if username is None:
        raise Exception("username is None")
    if date is None:
        raise Exception("date is None")
    if username not in _foodLogs:
        raise Exception("user not found")
    if date not in _foodLogs[username]:
        return None
    return _foodLogs[username][date]
