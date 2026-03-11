'''
Created on Mar 11, 2026

@author: Justin Smith
'''
from model.user import User
from model.food_log import FoodLog
from model.diet_goals import DietGoals

'''
Functions as the database for the application, storing all user and food information.
'''
_users = {} # maps username to another map that maps password to user
_foodItems = {} # maps food item descriptions to food items

def loadDefaultData():
    '''
    Initialize the database with defaut users and food items.
    '''
    foodLog1 = FoodLog("2026-03-11", ["oatmeal", "banana"], ["chicken salad"], ["grilled salmon", "steamed vegetables"], ["apple"])
    dietGoals1 = DietGoals("CALORIES", 2000, 150, 70, 50, 2300, 250, ["eat more vegetables"])
    user1 = User("John Doe", "johndoe", "password123", foodLog1, dietGoals1)
    _users[user1.getUsername()] = {user1.getPassword(): user1}
          
def getUsers():
    '''
    Get the users in the database.
        
    @return the users in the database.
    '''
    return _users
    
def getFoodItems():
    '''
    Get the foodItems in the database.
        
    @return the foodItems in the database.
    '''
    return _foodItems
    

def addUser(user):
    '''
    Add a user to the database.
            
    @precondition user != None
            
    @postcondition getUsers().contains(user)
            
    @param user the user to add to the database
    '''
    if (user == None):
        raise Exception("user is None")
    _users.append(user)
    
   
def addFoodItem(foodItem):
    '''
    Add a food item to the database.
        
    @precondition foodItem != None
        
    @postcondition getFoodItems().contains(foodItem)
        
    @param foodItem the food item to add to the database
    '''
    if(foodItem == None):
        raise Exception("food item is None")
    _foodItems.append(foodItem)