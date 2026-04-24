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

def loadDefaultData():
    '''
    Loads default data into the database for testing purposes.
    '''
    oatmeal = BaseFood("oatmeal", QuantityCategory.SERVING, 1, 150, 5, 3, 1, 27, 0)
    muffin = BaseFood("muffin", QuantityCategory.QUANTITY, 1, 400, 6, 20, 30, 50, 150)
    cereal = BaseFood("cereal", QuantityCategory.WEIGHT, 1, 120, 2, 1, 10, 25, 150)
    
    slice_of_bread = BaseFood("slice of bread", QuantityCategory.QUANTITY, 1, 80, 3, 1, 15, 14, 150)
    ham_slice = BaseFood("ham slice", QuantityCategory.QUANTITY, 1, 50, 5, 2, 0, 1, 100)
    turkey_slice = BaseFood("turkey slice", QuantityCategory.QUANTITY, 1, 30, 5, 1, 0, 1, 150)
    tomato_slice = BaseFood("tomato slice", QuantityCategory.QUANTITY, 1, 5, 0, 0, 1, 1, 50)
    sandwich = CompositeFood("sandwich", QuantityCategory.SERVING, 1, 200, 10, 5, 20, 40, 300, {slice_of_bread.get_description(): slice_of_bread, ham_slice.get_description(): ham_slice, turkey_slice.get_description(): turkey_slice, tomato_slice.get_description(): tomato_slice})
    
    chicken_breast = BaseFood("chicken breast", QuantityCategory.WEIGHT, 1, 165, 31, 4, 0, 0, 74)
    salad = BaseFood("salad", QuantityCategory.WEIGHT, 1, 100, 5, 2, 5, 10, 200)
    chicken_salad = CompositeFood("chicken salad", QuantityCategory.SERVING, 1, 350, 30, 20, 5, 10, 500, {chicken_breast.get_description(): chicken_breast, salad.get_description(): salad})
    grilled_salmon = BaseFood("grilled salmon", QuantityCategory.WEIGHT, 1, 200, 22, 12, 0, 0, 50)
    steamed_vegetables = BaseFood("steamed vegetables", QuantityCategory.WEIGHT, 1, 50, 2, 0, 5, 10, 100)
    beef_patty = BaseFood("beef patty", QuantityCategory.WEIGHT, 1, 250, 20, 15, 0, 0, 80)
    pickles = BaseFood("pickles", QuantityCategory.WEIGHT, 1, 10, 0, 0, 2, 3, 300)
    cheese_slice = BaseFood("cheese slice", QuantityCategory.QUANTITY, 1, 70, 5, 6, 0, 1, 150)
    ketchup = BaseFood("ketchup", QuantityCategory.WEIGHT, 1, 20, 0, 0, 5, 5, 150)
    hamburger_bun = BaseFood("hamburger bun", QuantityCategory.QUANTITY, 1, 120, 4, 2, 3, 22, 200)
    hamburger = CompositeFood("hamburger", QuantityCategory.SERVING, 1, 500, 30, 25, 5, 10, 500, {beef_patty.get_description(): beef_patty, pickles.get_description(): pickles, cheese_slice.get_description(): cheese_slice, ketchup.get_description(): ketchup, hamburger_bun.get_description(): hamburger_bun})
    french_fries = BaseFood("french fries", QuantityCategory.WEIGHT, 1, 300, 3, 15, 0, 40, 200)
    tortilla = BaseFood("tortilla", QuantityCategory.QUANTITY, 1, 100, 3, 2, 1, 20, 150)
    black_beans = BaseFood("black beans", QuantityCategory.WEIGHT, 1, 150, 10, 1, 0, 30, 200)
    onion = BaseFood("onion", QuantityCategory.QUANTITY, 1, 10, 0, 0, 2, 3, 50)
    cilantro = BaseFood("cilantro", QuantityCategory.QUANTITY, 1, 5, 0, 0, 1, 1, 10)
    tomato = BaseFood("tomato", QuantityCategory.QUANTITY, 1, 20, 1, 0, 4, 5, 100)
    taco = CompositeFood("taco", QuantityCategory.SERVING, 1, 400, 20, 10, 5, 50, 400, {tortilla.get_description(): tortilla, black_beans.get_description(): black_beans, onion.get_description(): onion, cilantro.get_description(): cilantro, tomato.get_description(): tomato})
    
    apple = BaseFood("apple", QuantityCategory.QUANTITY, 1, 95, 0, 0, 19, 25, 2)
    banana = BaseFood("banana", QuantityCategory.QUANTITY, 1, 105, 1, 0, 14, 27, 1)
    potato_chips = BaseFood("potato chips", QuantityCategory.WEIGHT, 1, 150, 2, 10, 15, 20, 150)
    pretzels = BaseFood("pretzels", QuantityCategory.WEIGHT, 1, 110, 3, 1, 0, 23, 300)
    
    _foodItems[oatmeal.get_description()] = oatmeal
    _foodItems[muffin.get_description()] = muffin
    _foodItems[banana.get_description()] = banana
    _foodItems[cereal.get_description()] = cereal
    _foodItems[slice_of_bread.get_description()] = slice_of_bread
    _foodItems[ham_slice.get_description()] = ham_slice
    _foodItems[turkey_slice.get_description()] = turkey_slice
    _foodItems[tomato_slice.get_description()] = tomato_slice
    _foodItems[sandwich.get_description()] = sandwich
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
    _foodItems[french_fries.get_description()] = french_fries
    _foodItems[tortilla.get_description()] = tortilla
    _foodItems[black_beans.get_description()] = black_beans
    _foodItems[onion.get_description()] = onion
    _foodItems[cilantro.get_description()] = cilantro
    _foodItems[tomato.get_description()] = tomato
    _foodItems[taco.get_description()] = taco
    _foodItems[potato_chips.get_description()] = potato_chips
    _foodItems[pretzels.get_description()] = pretzels

    foodLog1 = FoodLog(date(2026, 4, 19), [oatmeal, banana], [chicken_salad], [hamburger, french_fries], [apple])
    foodLog2 = FoodLog(date(2026, 4, 20), [muffin], [taco], [grilled_salmon, steamed_vegetables], [pretzels])
    foodLog3 = FoodLog(date.today(), [], [sandwich, apple], [], [potato_chips])
    dietGoals1 = DietGoals("CALORIE", 2000, 150, 70, 50, 2300, 250, ["eat more vegetables"])
    user1 = User("John Doe", "johndoe", "password123", foodLog3, dietGoals1)
    user1.addFoodLog(foodLog1)
    user1.addFoodLog(foodLog2)
    
    _users[user1.getUsername()] = {user1.getPassword(): user1}
    

def getUsers():
    '''
    Returns the users in the database.
    '''
    return _users

def getUser(username, password):
    '''
    Returns the user with the specified username and password if found in the database, otherwise raises an exception if the user is not found or the password is incorrect.
    
    @precondition username != None &&
                  password != None
                  
    @return the user with the specified username and password if found in the database.
    '''
    if username is None:
        raise Exception("username is None")
    if password is None:
        raise Exception("password is None")
    if username not in _users:
        raise KeyError("user not found")
    if password not in _users[username]:
        raise KeyError("incorrect password")
    return _users[username][password]

def getFoodItems():
    '''
    Returns the food items in the database.
    '''
    return _foodItems

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

def addFoodLog(username, password, foodLog):
    '''
    Adds a food log to the database for the specified user and date.
    
    @precondition username != None &&
                  password != None &&
                  foodLog != None
    '''
    if username is None:
        raise Exception("username is None")
    if password is None:
        raise Exception("password is None")
    if foodLog is None:
        raise Exception("food log is None")
    if username not in _users:
        raise Exception("user not found")
    if password not in _users[username]:
        raise Exception("incorrect password")
    user = _users[username][password]
    user.addFoodLog(foodLog)

def searchFoodItemByDescription(query):
    '''
    Searches the database for food items matching the provided query and returns a list of matching food items.
    
    @precondition query != None
    
    @return a list of food items matching the provided query.
    '''
    if query is None:
        raise Exception("query is None")
    if query == "":
        return _foodItems.values()

    foodItems = []
    for key, value in _foodItems.items():
        if query.lower() in key.lower():
            foodItems.append(value)
    return foodItems

def searchFoodLogByDate(username, password, date):
    '''
    Searches the database for the food log for the specified user and date and returns the food log if found.
    
    @precondition username != None &&
                  password != None &&
                  date != None
                  
    @return the food log for the specified user and date if found, otherwise None if no matching date is found for the user.
    '''
    if username is None:
        raise Exception("username is None")
    if password is None:
        raise Exception("password is None")
    if date is None:
        raise Exception("date is None")
    if username not in _users:
        raise Exception("user not found")
    if password not in _users[username]:
        raise Exception("incorrect password")
    user = _users[username][password]
    storedFoodLogs = user.getStoredFoodLogs()
    return storedFoodLogs[date] if date in storedFoodLogs else None
