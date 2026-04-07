'''
Created on Apr 3, 2026

@author: vfilpo :)
'''
from model import database
from model.base_food import BaseFood
from model.quantity_category import QuantityCategory
from server import constants

REQUIRED_KEYS = [
    constants.KEY_FOOD_DESCRIPTION,
    constants.KEY_FOOD_QUANTITY_CATEGORY,
    constants.KEY_FOOD_PORTION_SIZE,
    constants.KEY_FOOD_CALORIES,
    constants.KEY_FOOD_PROTEIN,
    constants.KEY_FOOD_FAT,
    constants.KEY_FOOD_SUGAR,
    constants.KEY_FOOD_CARBS,
    constants.KEY_FOOD_SODIUM,
]

CATEGORY_MAP = {
    constants.QUANTITY_CATEGORY_SERVING: QuantityCategory.SERVING,
    constants.QUANTITY_CATEGORY_WEIGHT: QuantityCategory.WEIGHT,
    constants.QUANTITY_CATEGORY_QUANTITY: QuantityCategory.QUANTITY,
}

def handleRequest(request):
    for key in REQUIRED_KEYS:
        if key not in request:
            return {
                constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
                constants.KEY_FAILURE_MESSAGE: "missing field: {0}".format(key)
            }

    description = request[constants.KEY_FOOD_DESCRIPTION]

    if description in database.getFoodItems():
        return {
            constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
            constants.KEY_FAILURE_MESSAGE: "food item already exists: {0}".format(description)
        }

    raw_category = request[constants.KEY_FOOD_QUANTITY_CATEGORY]
    category = CATEGORY_MAP.get(raw_category)

    if category is None:
        return {
            constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
            constants.KEY_FAILURE_MESSAGE: "invalid quantity category: {0}".format(raw_category)
        }

    try:
        food = BaseFood(
            description,
            category,
            request[constants.KEY_FOOD_PORTION_SIZE],
            request[constants.KEY_FOOD_CALORIES],
            request[constants.KEY_FOOD_PROTEIN],
            request[constants.KEY_FOOD_FAT],
            request[constants.KEY_FOOD_SUGAR],
            request[constants.KEY_FOOD_CARBS],
            request[constants.KEY_FOOD_SODIUM],
        )
    except Exception as e:
        return {
            constants.KEY_STATUS: constants.BAD_MESSAGE_STATUS,
            constants.KEY_FAILURE_MESSAGE: "failed to create food item: {0}".format(str(e))
        }

    database.addFoodItem(food)
    return {constants.KEY_STATUS: constants.SUCCESS_STATUS}