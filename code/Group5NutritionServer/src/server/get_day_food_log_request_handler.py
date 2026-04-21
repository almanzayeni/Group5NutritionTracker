'''
Created on Apr 20, 2026

@author: vfilpo :)
'''
from model import database
from server import constants

def handleRequest(request):
    if constants.KEY_USERNAME not in request:
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "no username"}
    if constants.KEY_DATE not in request:
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "no date"}

    username = request[constants.KEY_USERNAME]
    date_str = request[constants.KEY_DATE]  # expect "YYYY-MM-DD"

    users = database.getUsers()
    if username not in users:
        return {constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS, constants.KEY_FAILURE_MESSAGE: "user not found"}

    # _users is { username: { password: User } }, so grab the User object
    user = next(iter(users[username].values()))
    food_log = user.getCurrentFoodLog()

    # Only return the log if the date matches, otherwise return empty
    from datetime import date as dateType
    log_date = food_log.getDate()  # adjust to your FoodLog getter

    if str(log_date) != date_str:
        return {
            constants.KEY_STATUS: constants.SUCCESS_STATUS,
            constants.KEY_FOOD_LOG: {
                "date": date_str,
                "breakfast": [],
                "lunch": [],
                "dinner": [],
                "snacks": []
            }
        }

    return {
        constants.KEY_STATUS: constants.SUCCESS_STATUS,
        constants.KEY_FOOD_LOG: serialize_food_log(food_log)
    }

def serialize_food_log(food_log):
    return {
        "date": str(food_log.getDate()),
        "breakfast": [serialize_food_item(f) for f in food_log.getBreakfast()],
        "lunch": [serialize_food_item(f) for f in food_log.getLunch()],
        "dinner": [serialize_food_item(f) for f in food_log.getDinner()],
        "snacks": [serialize_food_item(f) for f in food_log.getSnacks()],
    }

def serialize_food_item(food_item):
    from model.composite_food import CompositeFood
    food_type = "composite" if isinstance(food_item, CompositeFood) else "base"
    
    return {
        "type": food_type,
        "description": food_item.get_description(),
        "calories": food_item.get_calories(),
        "protein": food_item.get_protein(),
        "fat": food_item.get_fat(),
        "sugar": food_item.get_sugar(),
        "carbohydrates": food_item.get_carbohydrates(),
        "sodium": food_item.get_sodium(),
        "portionSize": food_item.get_portion_size(),
        "quantityCategory": food_item.get_quantity_category().name,
    }